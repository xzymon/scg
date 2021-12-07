package com.xzymon.scg.communication.client.validator;

import com.xzymon.scg.communication.client.JsonHelper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class JsonValidationError {
	private static String DATE_FORMAT = JsonHelper.DATE_FORMAT_FOR_PARSER;
	private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT);

	private String identifier;
	private String textValue;
	private Long numericValue;
	private Boolean booleanValue;
	private String description;

	public static JsonValidationError businessValidationError(String message) {
		return new JsonValidationError(message);
	}

	public static JsonValidationError invalidJsonSyntax(String message) {
		return new JsonValidationError(String.format("Invalid JSON Syntax. %1$s", message));
	}

	public static JsonValidationError invalidElement(String jsonPath) {
		return new JsonValidationError(jsonPath, String.format("%1$s : element is unexpected, or at least unexpected here", jsonPath));
	}

	public static JsonValidationError requiredValueAbsent(String propertyName) {
		return new JsonValidationError(propertyName, String.format("Required value absent: %1$s", propertyName));
	}

	public static JsonValidationError dictionaryValueIncorrect(String propertyName, String value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } is from outside of allowed dictionary", propertyName, value));
	}

	public static JsonValidationError patternMatchingUnsatisfied(String propertyName, String value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } does not match to pattern", propertyName, value));
	}

	public static JsonValidationError lengthIncorrect(String propertyName, String value, int requiredLength) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } has incorrect length - correct would be : %3$d", propertyName, value, requiredLength));
	}

	public static JsonValidationError lengthAboveMax(String propertyName, String value, int max) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } has length greater than maximal allowed length : %3$d", propertyName, value, max));
	}

	public static JsonValidationError lengthUnderMin(String propertyName, String value, int min) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } has length less than minimal allowed length : %3$d", propertyName, value, min));
	}

	public static JsonValidationError dateValueIncorrect(String propertyName, String value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } is not correct date", propertyName, value));
	}

	public static JsonValidationError dateTimeValueIncorrect(String propertyName, String value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } is not correct dateTime", propertyName, value));
	}

	public static JsonValidationError timeValueIncorrect(String propertyName, String value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } is not correct time", propertyName, value));
	}

	public static JsonValidationError booleanValueIncorrect(String propertyName, Boolean value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$s } is incorrect", propertyName, value));
	}

	public static JsonValidationError intValueIncorrect(String propertyName, Integer value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$d } is incorrect", propertyName, value));
	}

	public static JsonValidationError intValueToLow(String propertyName, Integer numericValue, Integer boundaryValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$d } is less than minimal allowed value : %3$d", propertyName, numericValue, boundaryValue));
	}

	public static JsonValidationError intValueToHigh(String propertyName, Integer numericValue, Integer boundaryValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$d } is greater than maximal allowed value : %3$d", propertyName, numericValue, boundaryValue));
	}

	public static JsonValidationError longValueIncorrect(String propertyName, Long value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$d } is incorrect", propertyName, value));
	}

	public static JsonValidationError bigDecimalValueIncorrect(String propertyName, BigDecimal value) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value { %2$d } is incorrect", propertyName, value));
	}

//-----------------------------------

	public static JsonValidationError valueNotString(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotString(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotString(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is Long : %2$d", propertyName, longValue));
	}

	public static JsonValidationError valueNotString(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotString(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public static JsonValidationError valueNotString(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not String, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotBoolean(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotBoolean(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotBoolean(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is Long : %2$d", propertyName, longValue));
	}

	public static JsonValidationError valueNotBoolean(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotBoolean(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public static JsonValidationError valueNotBoolean(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Boolean, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotInteger(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotInteger(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotInteger(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is Long : %2$d", propertyName, longValue));
	}

	public static JsonValidationError valueNotInteger(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotInteger(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public static JsonValidationError valueNotInteger(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Integer, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotLong(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotLong(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotLong(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotLong(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotLong(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public static JsonValidationError valueNotLong(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not Long, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotBigDecimal(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotBigDecimal(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotBigDecimal(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is Long : %2$s", propertyName, longValue));
	}

	public static JsonValidationError valueNotBigDecimal(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotBigDecimal(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public static JsonValidationError valueNotBigDecimal(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not BigDecimal, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotJSONArray(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotJSONArray(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotJSONArray(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is Long : %2$d", propertyName, longValue));
	}

	public static JsonValidationError valueNotJSONArray(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotJSONArray(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotJSONArray(String propertyName, JSONObject objectValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONArray, but is JSONObject : %2$s", propertyName, objectValue));
	}

//-----------------------------------

	public static JsonValidationError valueNotJSONObject(String propertyName, Boolean booleanValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is Boolean : %2$s", propertyName, booleanValue));
	}

	public static JsonValidationError valueNotJSONObject(String propertyName, Integer intValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is Integer : %2$d", propertyName, intValue));
	}

	public static JsonValidationError valueNotJSONObject(String propertyName, Long longValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is Long : %2$d", propertyName, longValue));
	}

	public static JsonValidationError valueNotJSONObject(String propertyName, BigDecimal bigDecimalValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is BigDecimal : %2$s", propertyName, bigDecimalValue));
	}

	public static JsonValidationError valueNotJSONObject(String propertyName, String stringValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is String : %2$s", propertyName, stringValue));
	}

	public static JsonValidationError valueNotJSONObject(String propertyName, JSONArray arrayValue) {
		return new JsonValidationError(propertyName, String.format("%1$s : passed value type is not JSONObject, but is JSONArray : %2$s", propertyName, arrayValue));
	}

	public JsonValidationError() {
	}

	public JsonValidationError(String description) {
		this.description = description;
	}

	public JsonValidationError(String identifier, String description) {
		this.identifier = identifier;
		this.description = description;
	}

	public JsonValidationError(String identifier, String textValue, String description) {
		this.identifier = identifier;
		this.textValue = textValue;
		this.description = description;
	}

	public JsonValidationError(String identifier, Long numericValue, String description) {
		this.identifier = identifier;
		this.numericValue = numericValue;
		this.description = description;
	}

	public JsonValidationError(String identifier, Boolean booleanValue, String description) {
		this.identifier = identifier;
		this.booleanValue = booleanValue;
		this.description = description;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	public Long getNumericValue() {
		return numericValue;
	}

	public void setNumericValue(Long numericValue) {
		this.numericValue = numericValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
