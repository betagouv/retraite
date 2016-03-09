package controllers;

public class Administration extends SecuredRetraiteController {

	public static void registerUsager() {
		if (isNotSuperUser()) {
			unauthorizedAuthenticationRequired();
		}
		render();
	}

	private static boolean isNotSuperUser() {
		return !retrieveConnectedUser().login.equals("retraite");
	}
}
