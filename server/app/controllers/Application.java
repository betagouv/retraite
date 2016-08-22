package controllers;

import static controllers.utils.ControllersMiscUtils.computeActionQueryParams;
import static controllers.utils.ControllersMiscUtils.getLook;
import static controllers.utils.DataUnbinder.unbind;
import static controllers.utils.Look.GENERIC;
import static utils.dao.DaoChecklistFactory.createDaoChecklist;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import controllers.data.PostData;
import controllers.utils.Look;
import models.Checklist;
import play.Logger;
import play.modules.pdf.PDF;
import play.templates.Template;
import play.templates.TemplateLoader;
import utils.JsonUtils;
import utils.doc.ChecklistForDoc;
import utils.doc.ChecklistForDocConverter;
import utils.engine.RetraiteEngineFactory;
import utils.engine.data.CommonExchangeData;
import utils.engine.data.RenderData;
import utils.mail.MailSenderWithSendGrid;

public class Application extends RetraiteController {

	public static void process(final PostData postData) {
		try {
			_process(postData);
		} catch (final Exception e) {
			final String userAgent = request.headers.get("user-agent").value();
			final String requestInfos = request.toString() + " " + paramsAsStr();
			Logger.error(e, "Error processing request ! userAgent=" + userAgent + ", requestInfos=" + requestInfos);
			oupsPleaseComeBack();
		}
	}

	public static void oupsPleaseComeBack() {
		final Look look = getLook(params);
		render(look);
	}

	private static void _process(PostData postData) {
		if (postData == null) {
			postData = new PostData();
		}
		postData.hidden_userStatus = unbind(params.get("postData.hidden_userStatus"));
		final boolean test = params._contains("test");
		final boolean debug = params._contains("debug");
		final boolean force55 = postData.isForce55 = params._contains("force55");
		final Look look = postData.look = getLook(params);
		final String actionQueryParams = computeActionQueryParams(test, debug, look, force55);
		if (test) {
			Logger.warn("Traitement des données en mode TEST, recherche des régimes en BDD !");
		}
		final RenderData data = RetraiteEngineFactory.create(test).processToNextStep(postData);
		final String page = getPageNameForGoogleAnalytics(data);
		if (data.hidden_step.equals("displayCheckList")) {
			final String key = UUID.randomUUID().toString();
			putToCache(key, new DisplayCheckListData(data, page, actionQueryParams));
			// Redirection pour avoir une URL spécifique pour hotjar
			redirectToDisplayCheckList(key, test, debug, look, force55);
		} else {
			renderTemplate("Application/steps/" + data.hidden_step + ".html", data, test, debug, page, look, force55, actionQueryParams);
		}
	}

	private static String paramsAsStr() {
		return JsonUtils.toJsonDisablingHtmlEscaping(params)
				.replace("%5B", "[")
				.replace("%5D", "]")
				.replace("%2F", "/")
				.replace("%22", "\"");
	}

	/*
	 * On passe par cette méthode pour pouvoir mettre les paramètres à null si besoin et éviter qu'ils apparaissent dans l'URL de navigation
	 */
	private static void redirectToDisplayCheckList(final String key, final boolean _test, final boolean _debug, final Look _look, final boolean _force55) {
		final Boolean test = (_test ? true : null);
		final Boolean debug = (_debug ? true : null);
		final Look look = (_look.isNotGeneric() ? _look : null);
		final Boolean force55 = (_force55 ? true : null);
		displayCheckList(key, test, debug, look, force55);
	}

	public static void displayCheckList(final String key, final Boolean test, final Boolean debug, final Look _look, final Boolean force55) {
		final Look look = (_look == null ? GENERIC : _look);
		final DisplayCheckListData displayCheckListData = getFromCache(key);
		if (displayCheckListData == null) {
			displayExpired(test, debug, look, force55);
		}
		final CommonExchangeData data = displayCheckListData.data;
		final String page = displayCheckListData.page;
		final String actionQueryParams = displayCheckListData.actionQueryParams;
		renderTemplate("Application/steps/" + data.hidden_step + ".html", data, test, debug, page, look, force55, actionQueryParams);
	}

	public static void displayExpired(final Boolean test, final Boolean debug, final Look look, final Boolean force55) {
		final String actionQueryParams = computeActionQueryParams(test, debug, look, force55);
		render(test, debug, look, force55, actionQueryParams);
	}

	public static void sendMail(final PostData postData) {

		final boolean test = params._contains("test");
		final RenderData data = RetraiteEngineFactory.create(test).processToNextStep(postData);
		data.isPDF = true;

		final String htmlContent = "Bonjour,<br/><br/>Veuillez trouver ci-joint votre checklist !<br/><br/>L'Equipe <b>Parcours Retraite</b>";

		final File file = new File("parcours.pdf");
		final PDF.Options pdfOptions = createPdfOptions();
		PDF.writePDF(file, "Application/pdf.html", pdfOptions, data);

		new MailSenderWithSendGrid().sendMail("Parcours Retraite<envoi.retraite@sgmap.fr>", postData.email, "Mon parcours retraite", htmlContent, file);
		Logger.info("Mail envoyé à " + postData.email + " !");
		ok();
	}

	private static final boolean AS_HTML = false;
	private static final boolean RENDER_PDF_WITH_I_TEXT = false;

	public static void pdf(final PostData postData) {

		final boolean test = params._contains("test");
		postData.isPDF = true;
		final RenderData data = RetraiteEngineFactory.create(test).processToNextStep(postData);
		data.isPDF = true;

		if (AS_HTML || params._contains("html")) {
			// Rendu HTML pour mise au point
			render(data);
		}

		setResponseHeaderForPdfContentType();
		setResponseHeaderForAttachedPdf("Mes_demarches_retraite.pdf");

		if (RENDER_PDF_WITH_I_TEXT) {
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("data", data);

			renderPdfWith_iText("Application/pdf.html", params);
			ok();
		} else {
			// PDF.renderPDF() ne peut pas être utilisé car il écrase les headers fixés ci-dessus

			final PDF.Options pdfOptions = new PDF.Options();
			pdfOptions.FOOTER = "<table width='100%' style='font-size: 14px;'><tbody><tr>"
					+ "<td width='30%' align='left'>" + "" + "</td>"
					+ "<td width='30%' align='center'>Mes démarches retraite, pas à pas</td>"
					+ "<td width='30%' align='right'>Page <pagenumber>/<pagecount></td>"
					+ "</tr></tbody></table>";

			PDF.writePDF(response.out, data, pdfOptions);

			// final Map<String, Object> params = new HashMap<String, Object>();
			// params.put("data", data);
			// final String hmltResultWithFullCssPath = renderToHtml("Application/pdf.html", params);

			ok();
		}
	}

	// Méthodes privées

	private static String getPageNameForGoogleAnalytics(final RenderData data) {
		return data.hidden_step + ("displayLiquidateurQuestions".equals(data.hidden_step) ? "_" + data.hidden_liquidateurStep : "");
	}

	private static void setResponseHeaderForPdfContentType() {
		response.setHeader("Content-Type", "application/pdf");
	}

	private static void setResponseHeaderForAttachedPdf(final String filename) {
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
	}

	private static void renderPdfWith_iText(final String templateName, final Map<String, Object> params) {
		final String hmltResultWithFullCssPath = renderToHtml(templateName, params);
		try {
			final Document document = new Document();
			final PdfWriter writer = PdfWriter.getInstance(document, response.out);
			document.open();
			final InputStream inputStreamWithHtml = new ByteArrayInputStream(hmltResultWithFullCssPath.getBytes());
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStreamWithHtml);
			document.close();
		} catch (DocumentException | IOException e) {
			Logger.error(e, "Erreur pendant la génération du PDF");
			error(e);
		}
	}

	private static String renderToHtml(final String templateName, final Map<String, Object> params) {
		final Template template = TemplateLoader.load(template(templateName));
		final String hmltResult = template.render(params);
		final String hmltResultWithFullCssPath = completePublicPathWithCurrentDirectoryPath(hmltResult);
		return hmltResultWithFullCssPath;
	}

	private static String completePublicPathWithCurrentDirectoryPath(final String hmltResult) {
		return hmltResult.replaceAll("/public/", getCurrentPath() + "/public/");
	}

	private static String getCurrentPath() {
		final File f = new File(".");
		final String absolutePath = f.getAbsolutePath();
		return absolutePath.substring(0, absolutePath.length() - 2);
	}

	public static void generateDoc(final String checklistNom, final boolean published, final boolean pdf, final boolean viewPdfAsHtml) {
		final Checklist checklistFromBdd = createDaoChecklist().find(checklistNom, published);
		final ChecklistForDoc checklist = new ChecklistForDocConverter().convert(checklistFromBdd);
		final Look look = Look.GENERIC;
		final boolean noInfoCookie = true;
		if (pdf && !viewPdfAsHtml) {
			setResponseHeaderForPdfContentType();
			setResponseHeaderForAttachedPdf("Mes_demarches_retraite_" + checklistNom + "_documentation.pdf");

			if (RENDER_PDF_WITH_I_TEXT) {
				final Map<String, Object> params = new HashMap<String, Object>();
				params.put("checklist", checklist);
				params.put("published", published);
				params.put("look", look);
				params.put("noInfoCookie", noInfoCookie);
				params.put("pdf", pdf);

				renderPdfWith_iText("Application/generateDoc.html", params);
				ok();
			} else {
				// PDF.renderPDF() ne peut pas être utilisé car il écrase les headers fixés ci-dessus
				PDF.writePDF(response.out, checklist, published, look, noInfoCookie, pdf);
				ok();
			}
		} else {
			render(checklist, published, look, noInfoCookie);
		}
	}

	private static PDF.Options createPdfOptions() {
		final PDF.Options pdfOptions = new PDF.Options();
		pdfOptions.FOOTER = "<span style='font-size: 0.7em;'>Parcours Retraite</span>";
		return pdfOptions;
	}

	// Cache pour l'affichage des checklists

	private static final long TEN_MINUTES = 10 * 60 * 1000;

	private static Map<String, DisplayCheckListData> cache = new PassiveExpiringMap<>(TEN_MINUTES);

	private static class DisplayCheckListData {

		private final RenderData data;
		private final String page;
		private final String actionQueryParams;

		public DisplayCheckListData(final RenderData data, final String page, final String actionQueryParams) {
			this.data = data;
			this.page = page;
			this.actionQueryParams = actionQueryParams;
		}
	}

	private static void putToCache(final String key, final DisplayCheckListData displayCheckListData) {
		cleanCacheForTimeoutedData();
		cache.put(key, displayCheckListData);
	}

	private static DisplayCheckListData getFromCache(final String key) {
		cleanCacheForTimeoutedData();
		return cache.get(key);
	}

	private static void cleanCacheForTimeoutedData() {
		// Access all map to force clean
		cache.entrySet();
	}

}
