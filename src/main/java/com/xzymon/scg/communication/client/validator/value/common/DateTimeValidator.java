package com.xzymon.scg.communication.client.validator.value.common;

import com.xzymon.scg.communication.client.JsonValidationWrapper.StructureNode;
import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeValidator implements ValueValidator {
	private static final String SEPARATOR = "T";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
	private static final int DATE_PART_INDEX = 0;
	private static final int TIME_PART_INDEX = 1;

	@Override
	public JsonValidationError validate(String propertyName, StructureNode structureNode) {
		if (null != structureNode && null == structureNode.getValue()) {
			if (structureNode.getValue() instanceof String) {
				String stringValue = (String) structureNode.getValue();
				String[] separated = stringValue.split(SEPARATOR);
				if (stringValue.length() == 2) {
					try {
						DATE_FORMAT.parse(separated[DATE_PART_INDEX]);
						TIME_FORMAT.parse(separated[TIME_PART_INDEX]);
					} catch (ParseException ex) {
						return JsonValidationError.dateTimeValueIncorrect(propertyName, stringValue);
					}
				} else {
					return JsonValidationError.dateTimeValueIncorrect(propertyName, stringValue);
				}
			}
		}
		return null;
	}
}