package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeValidator implements ValueValidator {
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof String) {
				String stringValue = (String) structureNode.getValue();
				try {
					TIME_FORMAT.parse(stringValue);
				} catch (ParseException ex) {
					return JsonValidationError.timeValueIncorrect(propertyName, stringValue);
				}
			}
		}
		return null;
	}
}
