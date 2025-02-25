package eu.neclab.ngsildbroker.commons.datatypes.results;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.jsonldjava.core.Context;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import eu.neclab.ngsildbroker.commons.constants.NGSIConstants;
import eu.neclab.ngsildbroker.commons.datatypes.RemoteHost;
import eu.neclab.ngsildbroker.commons.exceptions.ResponseException;
import io.vertx.mutiny.core.MultiMap;

public class CRUDSuccess {
	protected Map<String, Object> json = Maps.newHashMap();
	private Set<Attrib> attribs;

	public CRUDSuccess(String endpoint, MultiMap headers, String cSourceId, Map<String, Object> entityAdded,
			Context context) {
		this(endpoint, headers, cSourceId, NGSILDOperationResult.getAttribs(entityAdded, context));
	}

	public CRUDSuccess(RemoteHost host, Set<Attrib> attribs) {
		this(host.host(), host.headers(), host.cSourceId(), attribs);
	}

	public CRUDSuccess(String endpoint, MultiMap headers, String cSourceId, Set<Attrib> attribs) {
		super();
		if (endpoint != null) {
			json.put(NGSIConstants.ERROR_DETAIL_ENDPOINT, endpoint);
		}
		if (cSourceId != null) {
			json.put(NGSIConstants.ERROR_DETAIL_CSOURCE_ID, cSourceId);
		}
		List<Map<String, String>> tmp = Lists.newArrayList();
		for (Attrib attrib : attribs) {
			tmp.add(attrib.getJson());
		}
		json.put(NGSIConstants.NGSI_LD_ATTRIBUTES_SHORT, tmp);
		this.attribs = attribs;

	}

	public Map<String, Object> getJson() {
		return json;
	}

	@SuppressWarnings("unchecked")
	public static CRUDSuccess fromPayload(Map<String, Object> entry) throws ResponseException {
		return new CRUDSuccess((String) entry.get(NGSIConstants.ERROR_DETAIL_ENDPOINT), null,
				(String) entry.get(NGSIConstants.ERROR_DETAIL_CSOURCE_ID),
				getAttribsFromPayload((List<Map<String, String>>) entry.get(NGSIConstants.NGSI_LD_ATTRIBUTES_SHORT)));
	}

	private static Set<Attrib> getAttribsFromPayload(List<Map<String, String>> list) throws ResponseException {
		Set<Attrib> result = Sets.newHashSet();
		for (Map<String, String> entry : list) {
			result.add(Attrib.fromPayload(entry));
		}
		return result;
	}

	public Set<Attrib> getAttribs() {
		return attribs;
	}

}
