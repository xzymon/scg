package com.xzymon.scg.communication.client;

import com.xzymon.scg.communication.client.validator.JsonValidationError;
import com.xzymon.scg.communication.client.validator.type.TypeValidator;
import com.xzymon.scg.communication.client.validator.value.ValueValidator;
import com.xzymon.scg.communication.client.validator.value.common.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class JsonValidationWrapper {
	private Map<String, StructureNode> flatStructureMap;
	private List<ExpectedNode> expectedNodesRegister;
	private List<StructureNode> unexpectedNodesRegister;
	private List<JsonValidationError> structuralValidationErrors;
	private List<JsonValidationError> typeValidationErrors;
	private List<JsonValidationError> independentValuesValidationErrors;
	private List<JsonValidationError> dependentValuesValidationErrors;

	public JsonValidationWrapper(JSONObject rootObject) {
		flatStructureMap = jsonPathFlatMap(rootObject);
		expectedNodesRegister = new ArrayList<>();
		structuralValidationErrors = new ArrayList<>();
		typeValidationErrors = new ArrayList<>();
		independentValuesValidationErrors = new ArrayList<>();
		dependentValuesValidationErrors = new ArrayList<>();
	}

	public static Map<String, StructureNode> jsonPathFlatMap(JSONObject rootObject) {
		Map<String, StructureNode> result = new LinkedHashMap<>();
		String pathRoot = JsonKeys.PATH_ROOT.getKey();
		List<String> tokenizedJsonPath = new ArrayList<>();
		tokenizedJsonPath.add(pathRoot);
		StructureNode rootNode = new StructureNode(pathRoot, tokenizedJsonPath, rootObject, null);

		recursiveJsonPathFlatMap(rootNode, rootObject, pathRoot, tokenizedJsonPath, result);

		return result;
	}

	private static void recursiveJsonPathFlatMap(StructureNode currentNode, JSONObject currentObject, String currentNodePath, List<String> tokenizedNodePath, Map<String, StructureNode> accumulatingFlatMap) {
		Object value;
		List<String> tokenizedPath;
		for (String key : currentObject.keySet()) {
			String flatKey = String.format("%1$s.%2$s", currentNodePath, key);
			tokenizedPath = new ArrayList<>();
			for (String token : tokenizedNodePath) {
				tokenizedPath.add(token);
			}
			tokenizedPath.add(key);
			value = currentObject.get(key);
			StructureNode valueNode = new StructureNode(flatKey, tokenizedPath, value, currentNode);
			accumulatingFlatMap.put(flatKey, valueNode);
			if (value != null) {
				if (value instanceof JSONArray) {
					nestIntoArray(valueNode, (JSONArray) value, flatKey, tokenizedPath, accumulatingFlatMap);
				}
				if (value instanceof JSONObject) {
					recursiveJsonPathFlatMap(valueNode, (JSONObject) value, flatKey, tokenizedPath, accumulatingFlatMap);
				}
			}
		}
	}

	private static void nestIntoArray(StructureNode currentNode, JSONArray array, String currentNodePath, List<String> tokenizedNodePath, Map<String, StructureNode> accumulatingFlatMap) {
		Object value;
		List<String> tokenizedPath;
		for (int loop = 0; loop < array.length(); loop++) {
			String flatKey = String.format("%1$s.[%2$d]", currentNodePath, loop);
			tokenizedPath = new ArrayList<>();
			for (String token : tokenizedNodePath) {
				tokenizedPath.add(token);
			}
			tokenizedPath.add(String.format("[%1$d]", loop));
			value = array.get(loop);
			StructureNode valueNode = new StructureNode(flatKey, tokenizedPath, value, currentNode);
			accumulatingFlatMap.put(flatKey, valueNode);
			if (value != null) {
				if (value instanceof JSONArray) {
					nestIntoArray(valueNode, (JSONArray) value, flatKey, tokenizedPath, accumulatingFlatMap);
				}
				if (value instanceof JSONObject) {
					recursiveJsonPathFlatMap(valueNode, (JSONObject) value, flatKey, tokenizedPath, accumulatingFlatMap);
				}
			}
		}
	}

	public ExpectedNodeBuilder expect(JsonKeys... keys) {
		ExpectedNodeBuilder builder = new ExpectedNodeBuilder(this);
		List<String> tokenizedPath = new ArrayList<>();
		tokenizedPath.add(JsonKeys.PATH_ROOT.getKey());
		JsonKeys key;
		for (int loop = 0; loop < keys.length; loop++) {
			key = keys[loop];
			tokenizedPath.add(key.getKey());
		}
		builder.setTokenizedPath(tokenizedPath);
		return builder;
	}

	public void checkExpectedKeysPrescence() {
		unexpectedNodesRegister = new ArrayList<>();
		List<StructureNode> expectedAmbiguousNodes = new ArrayList<>();
		List<StructureNode> expectedUnambiguousNodes = new ArrayList<>();
		for (Map.Entry<String, StructureNode> entry : flatStructureMap.entrySet()) {
			char[] cArr = entry.getKey().toCharArray();
			boolean ambi = false;
			for (int loop = 0; loop < cArr.length; loop++) {
				if (cArr[loop] == '[') {
					ambi = true;
					break;
				}
			}
			if (ambi) {
				expectedAmbiguousNodes.add(entry.getValue());
			} else {
				expectedUnambiguousNodes.add(entry.getValue());
			}
		}
		for (ExpectedNode expectedNode : expectedNodesRegister) {
			boolean ambi = false;
			for (String token : expectedNode.getTokenizedJsonPath()) {
				if (token.toCharArray()[0] == '[') {
					ambi = true;
					break;
				}
			}
			if (ambi) {
				expectedNode.setExists(searchForAmbiguousMatchingPath(expectedNode, expectedAmbiguousNodes));
			} else {
				StructureNode unambiguousNode = getStructureNodeWithUnambiguousMatchingPath(expectedNode, expectedUnambiguousNodes);
				if (unambiguousNode != null) {
					expectedNode.setStructureNode(unambiguousNode);
					expectedNode.setExists(true);
				}
			}
		}
		for (Map.Entry<String, StructureNode> entry : flatStructureMap.entrySet()) {
			if (!entry.getValue().isExpected()) {
				unexpectedNodesRegister.add(entry.getValue());
			}
		}
	}

	public void validateStructure() {
		structuralValidationErrors.clear();
		if (unexpectedNodesRegister.size() > 0) {
			for (StructureNode structureNode : unexpectedNodesRegister) {
				structuralValidationErrors.add(JsonValidationError.invalidElement(structureNode.getJsonPath()));
			}
		}
	}

	public boolean isStructureValid() {
		return structuralValidationErrors.size() == 0;
	}

	public void validateTypeOfNodes() {
		typeValidationErrors.clear();
		JsonValidationError error;
		for (StructureNode node : flatStructureMap.values()) {
			if (node.isExpected()) {
				error = node.getTypeValidator().validate(node.getJsonPath(), node.getValue());
				if (null != error) {
					typeValidationErrors.add(error);
				}
			}
		}
	}

	public boolean hasStructureValidTypes() {
		return typeValidationErrors.size() == 0;
	}

	public boolean searchForUnambiguousMatchingPath(ExpectedNode expectedNode, List<StructureNode> unambiguousStructureNodes) {
		List<String> tokenizedPath = expectedNode.getTokenizedJsonPath();
		for (StructureNode node : unambiguousStructureNodes) {
			List<String> tokens = node.getTokenizedJsonPath();
			if (tokenizedPath.size() <= tokens.size()) {
				for (int loop = 0; loop < tokenizedPath.size(); loop++) {
					String pattern = tokenizedPath.get(loop);
					String candidate = tokens.get(loop);
					if (pattern.equals(candidate)) {
						if (loop == tokenizedPath.size() - 1) {
							node.setExpected(true);
							node.setTypeValidator(expectedNode.getTypeValidator());
							setExpectedParents(node);
							return true;
						}
					} else {
						break;
					}
				}
			}
		}
		return false;
	}

	public StructureNode getStructureNodeWithUnambiguousMatchingPath(ExpectedNode expectedNode, List<StructureNode> unambiguousStructureNodes) {
		List<String> tokenizedPath = expectedNode.getTokenizedJsonPath();
		for (StructureNode node : unambiguousStructureNodes) {
			List<String> tokens = node.getTokenizedJsonPath();
			if (tokenizedPath.size() <= tokens.size()) {
				for (int loop = 0; loop < tokenizedPath.size(); loop++) {
					String pattern = tokenizedPath.get(loop);
					String candidate = tokens.get(loop);
					if (pattern.equals(candidate)) {
						if (loop == tokenizedPath.size() - 1) {
							node.setExpected(true);
							node.setTypeValidator(expectedNode.getTypeValidator());
							setExpectedParents(node);
							return node;
						}
					} else {
						break;
					}
				}
			}
		}
		return null;
	}

	public boolean searchForAmbiguousMatchingPath(ExpectedNode expectedNode, List<StructureNode> ambiguousStructureNodes) {
		List<String> tokenizedPath = expectedNode.getTokenizedJsonPath();
		boolean result = false;
		for (StructureNode node : ambiguousStructureNodes) {
			List<String> tokens = node.getTokenizedJsonPath();
			if (tokenizedPath.size() <= tokens.size()) {
				for (int loop = 0; loop < tokenizedPath.size(); loop++) {
					String pattern = tokenizedPath.get(loop);
					String candidate = tokens.get(loop);
					if (pattern.equals(candidate)) {
						if (loop == tokenizedPath.size() - 1) {
							node.setExpected(true);
							node.setTypeValidator(expectedNode.getTypeValidator());
							setExpectedParents(node);
							return true;
						}
					} else {
						if (pattern.equals(JsonKeys.SOME_ARRAY_ELEMENT.getKey()) && candidate.toCharArray()[0] == '[') {
							if (loop == tokenizedPath.size() - 1) {
								node.setExpected(true);
								node.setTypeValidator(expectedNode.getTypeValidator());
								setExpectedParents(node);
								result = true;
								break;
							} else {
								continue;
							}
						} else {
							break;
						}
					}
				}
			}
		}
		return result;
	}

	private void setExpectedParents(StructureNode childNode) {
		StructureNode node = childNode;
		while (node.getParent() != null && !node.getParent().isExpected()) {
			node = node.getParent();
			node.setExpected(true);
		}
	}

	public void validateIndependentValuesOfExpectedNodes() {
		independentValuesValidationErrors.clear();
		JsonValidationError validationError = null;
		for (ExpectedNode expectedNode : getExpectedNodesRegister()) {
			for (ValueValidator valueValidator : expectedNode.getIndependentValueValidators()) {
				validationError = valueValidator.validate(expectedNode.getPropertyName(), expectedNode.getStructureNode());
				if (null != validationError) {
					independentValuesValidationErrors.add(validationError);
				}
			}
		}
	}

	public Map<String, StructureNode> getFlatStructureMap() {
		return flatStructureMap;
	}

	public void setFlatStructureMap(Map<String, StructureNode> flatStructureMap) {
		this.flatStructureMap = flatStructureMap;
	}

	public List<ExpectedNode> getExpectedNodesRegister() {
		return expectedNodesRegister;
	}

	public void setExpectedNodesRegister(List<ExpectedNode> expectedNodesRegister) {
		this.expectedNodesRegister = expectedNodesRegister;
	}

	public List<StructureNode> getUnexpectedNodesRegister() {
		return unexpectedNodesRegister;
	}

	public void setUnexpectedNodesRegister(List<StructureNode> unexpectedNodesRegister) {
		this.unexpectedNodesRegister = unexpectedNodesRegister;
	}

	public List<JsonValidationError> getStructuralValidationErrors() {
		return structuralValidationErrors;
	}

	public void setStructuralValidationErrors(List<JsonValidationError> structuralValidationErrors) {
		this.structuralValidationErrors = structuralValidationErrors;
	}

	public List<JsonValidationError> getTypeValidationErrors() {
		return typeValidationErrors;
	}

	public void setTypeValidationErrors(List<JsonValidationError> typeValidationErrors) {
		this.typeValidationErrors = typeValidationErrors;
	}

	public List<JsonValidationError> getIndependentValuesValidationErrors() {
		return independentValuesValidationErrors;
	}

	public boolean hasIndependentValuesValidationErrors() {
		return independentValuesValidationErrors.size() != 0;
	}

	public void setIndependentValuesValidationErrors(List<JsonValidationError> independentValuesValidationErrors) {
		this.independentValuesValidationErrors = independentValuesValidationErrors;
	}

	public List<JsonValidationError> getDependentValuesValidationErrors() {
		return dependentValuesValidationErrors;
	}

	public void setDependentValuesValidationErrors(List<JsonValidationError> dependentValuesValidationErrors) {
		this.dependentValuesValidationErrors = dependentValuesValidationErrors;
	}

	public static class StructureNode {
		private String jsonPath;
		private List<String> tokenizedJsonPath;
		private boolean expected;
		private TypeValidator typeValidator;
		private Object value;
		private StructureNode parent;

		public StructureNode() {
		}

		public StructureNode(String jsonPath, List<String> tokenizedJsonPath, Object value, StructureNode parent) {
			this.jsonPath = jsonPath;
			this.tokenizedJsonPath = tokenizedJsonPath;
			this.value = value;
			this.parent = parent;
		}

		public String getJsonPath() {
			return jsonPath;
		}

		public void setJsonPath(String jsonPath) {
			this.jsonPath = jsonPath;
		}

		public List<String> getTokenizedJsonPath() {
			return tokenizedJsonPath;
		}

		public void setTokenizedJsonPath(List<String> tokenizedJsonPath) {
			this.tokenizedJsonPath = tokenizedJsonPath;
		}

		public boolean isExpected() {
			return expected;
		}

		public void setExpected(boolean expected) {
			this.expected = expected;
		}

		public TypeValidator getTypeValidator() {
			return typeValidator;
		}

		public void setTypeValidator(TypeValidator typeValidator) {
			this.typeValidator = typeValidator;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public StructureNode getParent() {
			return parent;
		}

		public void setParent(StructureNode parent) {
			this.parent = parent;
		}
	}

	public static class ExpectedNode {
		private List<String> tokenizedJsonPath;
		private TypeValidator typeValidator;
		private List<ValueValidator> independentValueValidators;
		private Map<String, Object> dependenciesMap;
		private boolean exists;
		private StructureNode structureNode;

		public ExpectedNode() {
		}

		public ExpectedNode(List<String> tokenizedJsonPath, TypeValidator typeValidator) {
			this.tokenizedJsonPath = tokenizedJsonPath;
			this.typeValidator = typeValidator;
		}

		public ExpectedNode(List<String> tokenizedJsonPath, TypeValidator typeValidator, List<ValueValidator> independentValueValidators, Map<String, Object> dependenciesMap) {
			this.tokenizedJsonPath = tokenizedJsonPath;
			this.typeValidator = typeValidator;
			this.independentValueValidators = independentValueValidators;
			this.dependenciesMap = dependenciesMap;
		}

		public List<String> getTokenizedJsonPath() {
			return tokenizedJsonPath;
		}

		public void setTokenizedJsonPath(List<String> tokenizedJsonPath) {
			this.tokenizedJsonPath = tokenizedJsonPath;
		}

		public TypeValidator getTypeValidator() {
			return typeValidator;
		}

		public void setTypeValidator(TypeValidator typeValidator) {
			this.typeValidator = typeValidator;
		}

		public List<ValueValidator> getIndependentValueValidators() {
			return independentValueValidators;
		}

		public void setIndependentValueValidators(List<ValueValidator> independentValueValidators) {
			this.independentValueValidators = independentValueValidators;
		}

		public Map<String, Object> getDependenciesMap() {
			return dependenciesMap;
		}

		public void setDependenciesMap(Map<String, Object> dependenciesMap) {
			this.dependenciesMap = dependenciesMap;
		}

		public boolean isExists() {
			return exists;
		}

		public void setExists(boolean exists) {
			this.exists = exists;
		}

		public StructureNode getStructureNode() {
			return structureNode;
		}

		public void setStructureNode(StructureNode structureNode) {
			this.structureNode = structureNode;
		}

		public String getPropertyName() {
			if (null != tokenizedJsonPath && tokenizedJsonPath.size() > 0) {
				return tokenizedJsonPath.get(tokenizedJsonPath.size() - 1);
			}
			return null;
		}
	}

	public static class ExpectedNodeBuilder {
		private JsonValidationWrapper holder;
		private List<String> tokenizedPath;
		private TypeValidator typeValidator;
		private List<ValueValidator> independentValueValidators;
		private Map<String, Object> dependenciesMap;

		public ExpectedNodeBuilder(JsonValidationWrapper holder) {
			this.holder = holder;
			this.independentValueValidators = new ArrayList<>();
			this.dependenciesMap = new HashMap<>();
		}

		public JsonValidationWrapper getHolder() {
			return holder;
		}

		public void setHolder(JsonValidationWrapper holder) {
			this.holder = holder;
		}

		public List<String> getTokenizedPath() {
			return tokenizedPath;
		}

		public void setTokenizedPath(List<String> tokenizedPath) {
			this.tokenizedPath = tokenizedPath;
		}

		public TypeValidator getTypeValidator() {
			return typeValidator;
		}

		public void setTypeValidator(TypeValidator typeValidator) {
			this.typeValidator = typeValidator;
		}

		public List<ValueValidator> getIndependentValueValidators() {
			return independentValueValidators;
		}

		public void setIndependentValueValidators(List<ValueValidator> independentValueValidators) {
			this.independentValueValidators = independentValueValidators;
		}

		public ExpectedNodeBuilder dependsOn(String dependencyName, Object dependencyValue) {
			dependenciesMap.put(dependencyName, dependencyValue);
			return this;
		}

		public ExpectedNodeBuilder required() {
			this.independentValueValidators.add(new RequiredValidator());
			return this;
		}

		public ExpectedNodeBuilder dictionary(String[] allowedValues) {
			this.independentValueValidators.add(new DictionaryValidator(allowedValues));
			return this;
		}

		public ExpectedNodeBuilder pattern(String pattern) {
			this.independentValueValidators.add(new PatternValidator(pattern));
			return this;
		}

		public ExpectedNodeBuilder date() {
			this.independentValueValidators.add(new DateValidator());
			return this;
		}

		public ExpectedNodeBuilder dateTime() {
			this.independentValueValidators.add(new DateTimeValidator());
			return this;
		}

		public ExpectedNodeBuilder time() {
			this.independentValueValidators.add(new TimeValidator());
			return this;
		}

		public ExpectedNodeBuilder length(int requiredLength) {
			this.independentValueValidators.add(new LengthValidator(requiredLength));
			return this;
		}

		public ExpectedNodeBuilder lengthMax(int maxLength) {
			this.independentValueValidators.add(new MaxLengthValidator(maxLength));
			return this;
		}

		public ExpectedNodeBuilder lengthMin(int minLength) {
			this.independentValueValidators.add(new MinLengthValidator(minLength));
			return this;
		}

		public ExpectedNodeBuilder max(int maxValue) {
			this.independentValueValidators.add(new MaxValidator(maxValue));
			return this;
		}

		public ExpectedNodeBuilder min(int minValue) {
			this.independentValueValidators.add(new MinValidator(minValue));
			return this;
		}

		public JsonValidationWrapper end() {
			this.holder.getExpectedNodesRegister().add(new ExpectedNode(this.tokenizedPath, this.typeValidator, this.independentValueValidators, this.dependenciesMap));
			return this.holder;
		}
	}
}
