package eu.neclab.ngsildbroker.commons.datatypes.requests;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

	private String tenant;
	protected Map<String, Object> payload = new HashMap<>();
	private int requestType;
	private long sendTimestamp = System.currentTimeMillis();

	private String id;
	private Map<String, Object> previousEntity;
	protected String attribName;
	protected String datasetId;
	protected boolean deleteAll;

	public BaseRequest() {

	}

	protected BaseRequest(String tenant, String id, Map<String, Object> requestPayload, int requestType) {
		super();
		this.tenant = tenant;
		this.requestType = requestType;
		this.id = id;
		this.payload = requestPayload;
	}

	public int getRequestType() {
		return requestType;
	}

	public long getSendTimestamp() {
		return sendTimestamp;
	}

	public void setSendTimestamp(long sendTimestamp) {
		this.sendTimestamp = sendTimestamp;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPreviousEntity(Map<String, Object> previousEntity) {
		this.previousEntity = previousEntity;
	}

	public Map<String, Object> getPreviousEntity() {
		return previousEntity;
	}

	public String getAttribName() {
		return attribName;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public boolean deleteAll() {
		return deleteAll;
	}

	public boolean isDeleteAll() {
		return deleteAll;
	}

	public void setDeleteAll(boolean deleteAll) {
		this.deleteAll = deleteAll;
	}

	public void setAttribName(String attribName) {
		this.attribName = attribName;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

}
