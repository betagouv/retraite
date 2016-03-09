package utils.wsinforetraite;

public interface InfoRetraite {

	public abstract InfoRetraiteResult retrieveAllInformations(String name, String nir, String dateNaissance);

	public abstract String retrieveRegimes(String name, String nir, String dateNaissance);

}