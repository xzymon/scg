package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;

public class LongValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotLong(jsonPath, (Boolean) value);
			}
			if (value instanceof String) {
				return JsonValidationError.valueNotLong(jsonPath, (String) value);
			}
			if (value instanceof BigDecimal) {
				return JsonValidationError.valueNotLong(jsonPath, (BigDecimal) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotLong(jsonPath, (JSONArray) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotLong(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
