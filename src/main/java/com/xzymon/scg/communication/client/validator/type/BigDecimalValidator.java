package com.xzymon.scg.communication.client.validator.type;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import org.json.JSONArray;
import org.json.JSONObject;

public class BigDecimalValidator implements TypeValidator {

	@Override
	public JsonValidationError validate(String jsonPath, Object value) {
		if (value != null) {
			if (value instanceof Boolean) {
				return JsonValidationError.valueNotBigDecimal(jsonPath, (Boolean) value);
			}
			if (value instanceof String) {
				return JsonValidationError.valueNotBigDecimal(jsonPath, (String) value);
			}
			if (value instanceof JSONArray) {
				return JsonValidationError.valueNotBigDecimal(jsonPath, (JSONArray) value);
			}
			if (value instanceof JSONObject) {
				return JsonValidationError.valueNotBigDecimal(jsonPath, (JSONObject) value);
			}
		}
		return null;
	}
}
