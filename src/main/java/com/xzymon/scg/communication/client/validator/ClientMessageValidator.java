package com.xzymon.scg.communication.client.validator;

import com.xzymon.scg.communication.client.JsonKeys;
import com.xzymon.scg.communication.client.JsonValidationWrapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageValidator {

	public static List<JsonValidationError> validate(JSONObject rawJsonObject) {
		List<JsonValidationError> jsonValidationErrors = new ArrayList<>();

		JsonValidationWrapper jsonValidationWrapper = new JsonValidationWrapper(rawJsonObject);

		//TODO: construct what to expect on nodes
		jsonValidationWrapper
				.expect(JsonKeys.GAME_EVENT).objectType().end()
				.expect(JsonKeys.GAME_EVENT, JsonKeys.NAME).stringType().end()
		;

		jsonValidationWrapper.checkExpectedKeysPrescence();

		jsonValidationWrapper.validateStructure();

		if (!jsonValidationWrapper.isStructureValid()) {
			return jsonValidationWrapper.getStructuralValidationErrors();
		}

		jsonValidationWrapper.validateTypeOfNodes();

		if (!jsonValidationWrapper.hasStructureValidTypes()) {
			return jsonValidationWrapper.getTypeValidationErrors();
		}

		//TODO: more sophisticated validations
		jsonValidationWrapper.validateIndependentValuesOfExpectedNodes();
		if (jsonValidationWrapper.hasIndependentValuesValidationErrors()) {
			return jsonValidationWrapper.getIndependentValuesValidationErrors();
		}

		return jsonValidationErrors;
	}
}
