package utils;

import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;

/**
 * Cette classe est une classe utilitaire permettant d'encapsuler les appels statiques à WS, pour permettre de bons tests unitaires, notamment de
 * InfoRetraiteConnector. Cette classe n'est pas testée, justement à cause des appels statiques à WS. PowerMock pourrait permettre de faire des TU sur cette
 * classe, mais les risques sont minmimes (cette classe contient peu de code et ne bouge jamais) et le cout des TU avec PowerMock est trop important
 *
 * @author xnopre
 */
public class WsUtils {

	public static WsParam param(final String name, final String value) {
		return new WsParam(name, value);
	}

	public HttpResponse doGet(final String baseUrl, final WsParam... params) {
		final WSRequest request = WS.url(baseUrl);
		for (final WsParam wsParam : params) {
			request.setParameter(wsParam.name, wsParam.value);
		}
		return request.get();
	}

	public static class WsParam {

		private final String name;
		private final String value;

		public WsParam(final String name, final String value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final WsParam other = (WsParam) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

	}

}
