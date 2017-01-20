package utils.wsinforetraite;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cnav.ods.service.externe.dai.annuaire.Affiliation;
import cnav.ods.service.externe.dai.annuaire.AnnuaireDaiWS;
import cnav.ods.service.externe.dai.annuaire.RendreAnnuRNirElement;
import cnav.ods.service.externe.dai.annuaire.SortieAnnuR;
import cnav.ods.util.ServiceLocator;
import cnav.ods.util.webservice.WebServiceWrapper;
import helper.AnnuaireDaiSimpleFactory;
import utils.RetraiteException;
import utils.wsinforetraite.InfoRetraiteResult.InfoRetraiteResultRegime;

public class InfoRetraiteAnnuaireDai implements InfoRetraite {
	
	private static final Logger LOG = LoggerFactory.getLogger(InfoRetraiteResult.class);

	@Override
	public InfoRetraiteResult retrieveAllInformations(String name, String nir, String dateNaissance) {
		
		LOG.debug("retrieveAllInformations [{} - {} - {}]", name, nir, dateNaissance);
				
		try {
		
			AnnuaireDaiWS stub  = (AnnuaireDaiWS) ((WebServiceWrapper) ServiceLocator.getService("ANNUAIRE-WS")).getWebServiceClient(AnnuaireDaiSimpleFactory.class, "AnnuaireDaiWS");
			
			RendreAnnuRNirElement rendreAnnuRNirElement = new RendreAnnuRNirElement();
			rendreAnnuRNirElement.setNir(nir.replaceAll(" ", "").substring(0, 13));
			rendreAnnuRNirElement.setNom(name);
			rendreAnnuRNirElement.setCdRegime(2294); //TODO
			
			SortieAnnuR resultat = stub.rendreAnnuRNir(rendreAnnuRNirElement).getResult();
			
			LOG.debug("appel WS ok");
			
			InfoRetraiteResult infoRetraiteResult = null; 
			List<InfoRetraiteResultRegime> infoRetraiteResultRegimeList = null;
			if (resultat.getResAnnuR().getTabAffiliation() != null) {
				infoRetraiteResultRegimeList = new ArrayList<>();
				for (Affiliation affiliation : resultat.getResAnnuR().getTabAffiliation()) {
					InfoRetraiteResultRegime infoRetraiteResultRegime = new InfoRetraiteResultRegime();
					infoRetraiteResultRegime.regime = StringUtils.leftPad(String.valueOf(affiliation.getCdGes0X()), 4, '0');
					infoRetraiteResultRegime.nom = affiliation.getLibGesLibLong();
					infoRetraiteResultRegimeList.add(infoRetraiteResultRegime);
				}
				infoRetraiteResult = new InfoRetraiteResult(InfoRetraiteResult.Status.FOUND, infoRetraiteResultRegimeList.toArray(new InfoRetraiteResultRegime[infoRetraiteResultRegimeList.size()]));
			}
			
			
			
			return infoRetraiteResult;

		} catch (final Exception e) {
			LOG.error("Exception {}", e);
			throw new RetraiteException("Error requesting WS conseiller.info-retraite.fr", e);
		}
		
	}

}
