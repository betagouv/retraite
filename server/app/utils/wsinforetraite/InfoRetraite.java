package utils.wsinforetraite;

public interface InfoRetraite {

	public abstract InfoRetraiteResult retrieveAllInformations(String name, String nir, String dateNaissance);

}