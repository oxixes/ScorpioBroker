package eu.neclab.ngsildbroker.commons.datatypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Lists;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.MultiMap;

public class QueryRemoteHost {
	String host;
	String tenant;
	MultiMap headers;
	String cSourceId;
	boolean canDoSingleOp;
	boolean canDoBatchOp;
	int regMode;
	String queryString;
	boolean canDoEntityMap;
	boolean canDoZip;
	String remoteToken;
	

	public QueryRemoteHost(String host, String tenant, MultiMap headers, String cSourceId, boolean canDoSingleOp,
			boolean canDoBatchOp, int regMode, String queryString, boolean canDoEntityMap, boolean canDoZip,
			String remoteToken) {
		this.host = host;
		this.tenant = tenant;
		this.headers = headers;
		this.cSourceId = cSourceId;
		this.canDoSingleOp = canDoSingleOp;
		this.canDoBatchOp = canDoBatchOp;
		this.regMode = regMode;
		this.queryString = queryString;
		this.canDoEntityMap = canDoEntityMap;
		this.canDoZip = canDoZip;
		this.remoteToken = remoteToken;
	}

	public JsonObject toJson() {
		JsonObject result = new JsonObject();
		List<String[]> headersToStore;
		if (headers == null) {
			headersToStore = null;
		} else {
			headersToStore = Lists.newArrayList();
			for (Entry<String, String> header : headers.entries()) {
				headersToStore.add(new String[] { header.getKey(), header.getValue() });
			}
		}
		result.put("h", host);
		result.put("t", tenant);
		result.put("k", headersToStore); // k for kopf german for header since h is taken
		result.put("c", cSourceId);
		result.put("s", canDoSingleOp);
		result.put("b", canDoBatchOp);
		result.put("r", regMode);
		result.put("q", queryString);
		result.put("i", canDoEntityMap);
		result.put("z", canDoZip);
		result.put("o", remoteToken); // o because r and t are taken and i can't think of something sensible
		return result;
	}

	public boolean isLocal() {
		return host == null;
	}

	public static List<QueryRemoteHost> getRemoteHostsFromJson(List list) {
		List<QueryRemoteHost> result = new ArrayList<>(list.size());
		for (Object obj : list) {
			Map<String, Object> map = (Map<String, Object>) obj;
			String host = null;
			String tenant = null;
			MultiMap headers = null;
			String cSourceId = null;
			boolean canDoSingleOp = false;
			boolean canDoBatchOp = false;
			int regMode = -1;
			String queryString = null;
			boolean canDoIdQuery = false;
			boolean canDoZip = false;
			String remoteToken = null;
			for (Entry<String, Object> entry : map.entrySet()) {
				Object value = entry.getValue();
				switch (entry.getKey()) {
				case "h":
					host = value == null ? null : (String) value;
					break;
				case "t":
					tenant = (String) entry.getValue();
					break;
				case "k":
					if (value != null) {
						List<Map<String, String>> tmp = (List<Map<String, String>>) value;
						headers = new MultiMap(null);
						for (Map<String, String> header : tmp) {
							Entry<String, String> headerEntry = header.entrySet().iterator().next();
							headers.add(headerEntry.getKey(), headerEntry.getValue());
						}
					}
				case "c":
					cSourceId = value == null ? null : (String) value;
					break;
				case "s":
					canDoSingleOp = value == null ? false : (Boolean) value;
					break;
				case "b":
					canDoBatchOp = value == null ? false : (Boolean) value;
					break;
				case "r":
					regMode = value == null ? -1 : (Integer) value;
					break;
				case "q":
					queryString = value == null ? null : (String) value;
					break;
				case "i":
					canDoIdQuery = value == null ? false : (Boolean) value;
					break;
				case "z":
					canDoZip = value == null ? false : (Boolean) value;
					break;
				case "o":
					remoteToken = value == null ? null : (String) value;
					break;
				default:
					break;
				}
			}
			result.add(new QueryRemoteHost(host, tenant, headers, cSourceId, canDoSingleOp, canDoBatchOp, regMode,
					queryString, canDoIdQuery, canDoZip, remoteToken));
		}
		return result;
	}

	public static QueryRemoteHost fromRemoteHost(RemoteHost remoteHost, String queryString, boolean canDoIdQuery,
			boolean canDoZip, String remoteToken) {
		String finalHost = remoteHost.host();
		if (remoteHost.host().endsWith("/")) {
			finalHost = remoteHost.host().substring(0, remoteHost.host().length() - 1);
		}
		return new QueryRemoteHost(finalHost, remoteHost.tenant(), remoteHost.headers(), remoteHost.cSourceId(),
				remoteHost.canDoSingleOp(), remoteHost.canDoBatchOp(), remoteHost.regMode(), queryString, canDoIdQuery,
				canDoZip, remoteToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cSourceId, canDoBatchOp, canDoEntityMap, canDoSingleOp, canDoZip, host, queryString,
				regMode, remoteToken, tenant);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryRemoteHost other = (QueryRemoteHost) obj;
		return Objects.equals(cSourceId, other.cSourceId) && canDoBatchOp == other.canDoBatchOp
				&& canDoEntityMap == other.canDoEntityMap && canDoSingleOp == other.canDoSingleOp
				&& canDoZip == other.canDoZip && Objects.equals(host, other.host)
				&& Objects.equals(queryString, other.queryString) && regMode == other.regMode
				&& Objects.equals(remoteToken, other.remoteToken) && Objects.equals(tenant, other.tenant);
	}

	public QueryRemoteHost updatedDuplicate(String queryString) {
		return new QueryRemoteHost(host, tenant, headers, cSourceId, canDoSingleOp, canDoBatchOp, regMode, queryString,
				canDoEntityMap, canDoZip, remoteToken);
	}

	public void updatedIds(Set<String> remoteQEntityIds) {
		StringBuilder newQueryString = new StringBuilder();
		int idIdx = this.queryString.indexOf("id=");
		if (idIdx != -1) {
			int endIdIdx = this.queryString.indexOf("&", idIdx);
			newQueryString.append(this.queryString.substring(0, idIdx));
			newQueryString.append("id=");
			newQueryString.append(String.join(",", remoteQEntityIds));
			if (endIdIdx != -1) {
				newQueryString.append(this.queryString.substring(endIdIdx));
			}
		} else {
			newQueryString.append("&id=");
			newQueryString.append(String.join(",", remoteQEntityIds));
		}
		this.queryString = newQueryString.toString();

	}

	public String host() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String tenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public MultiMap headers() {
		return headers;
	}

	public void setHeaders(MultiMap headers) {
		this.headers = headers;
	}

	public String cSourceId() {
		return cSourceId;
	}

	public void setcSourceId(String cSourceId) {
		this.cSourceId = cSourceId;
	}

	public boolean canDoSingleOp() {
		return canDoSingleOp;
	}

	public void setCanDoSingleOp(boolean canDoSingleOp) {
		this.canDoSingleOp = canDoSingleOp;
	}

	public boolean canDoBatchOp() {
		return canDoBatchOp;
	}

	public void setCanDoBatchOp(boolean canDoBatchOp) {
		this.canDoBatchOp = canDoBatchOp;
	}

	public int regMode() {
		return regMode;
	}

	public void setRegMode(int regMode) {
		this.regMode = regMode;
	}

	public String queryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public boolean canDoEntityMap() {
		return canDoEntityMap;
	}

	public void setCanDoEntityMap(boolean canDoEntityMap) {
		this.canDoEntityMap = canDoEntityMap;
	}

	public boolean canDoZip() {
		return canDoZip;
	}

	public void setCanDoZip(boolean canDoZip) {
		this.canDoZip = canDoZip;
	}

	public String remoteToken() {
		return remoteToken;
	}

	public void setRemoteToken(String remoteToken) {
		this.remoteToken = remoteToken;
	}
	
	

}
