package eu.neclab.ngsildbroker.entityhandler.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.jsonldjava.core.JsonLdProcessor;
import eu.neclab.ngsildbroker.commons.tools.ControllerFunctions;
import eu.neclab.ngsildbroker.entityhandler.services.EntityService;

@RestController
@RequestMapping("/ngsi-ld/v1/entityOperations")
public class EntityBatchController {

	@Autowired
	EntityService entityService;

	@Value("${batchoperations.maxnumber.create:-1}")
	int maxCreateBatch;
	@Value("${batchoperations.maxnumber.update:-1}")
	int maxUpdateBatch;
	@Value("${batchoperations.maxnumber.upsert:-1}")
	int maxUpsertBatch;
	@Value("${batchoperations.maxnumber.delete:-1}")
	int maxDeleteBatch;

	@Value("${ngsild.corecontext:https://uri.etsi.org/ngsi-ld/v1/ngsi-ld-core-context.jsonld}")
	String coreContext;

	@PostConstruct
	public void init() {
		JsonLdProcessor.init(coreContext);
	}

	@PostMapping("/create")
	public ResponseEntity<String> createMultiple(HttpServletRequest request, @RequestBody String payload) {
		return ControllerFunctions.createMultiple(entityService, request, payload, maxCreateBatch);
	}

	@PostMapping("/upsert")
	public ResponseEntity<String> upsertMultiple(HttpServletRequest request, @RequestBody String payload,
			@RequestParam(required = false, name = "options") String options) {
		return ControllerFunctions.upsertMultiple(entityService, request, payload, options, maxCreateBatch);
	}

	@PostMapping("/update")
	public ResponseEntity<String> updateMultiple(HttpServletRequest request, @RequestBody String payload,
			@RequestParam(required = false, name = "options") String options) {
		return ControllerFunctions.updateMultiple(entityService, request, payload, maxUpdateBatch, options);
	}

	@PostMapping("/delete")
	public ResponseEntity<String> deleteMultiple(HttpServletRequest request, @RequestBody String payload) {
		return ControllerFunctions.deleteMultiple(entityService, request, payload);
	}

}
