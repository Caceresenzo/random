package com.example.custompathvariable.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import com.example.custompathvariable.resolver.domain.RoundRepository;
import com.example.custompathvariable.resolver.id.RoundIdentifier;

//@Component
public class UserIdentifierRequestParamResolver extends AbstractNamedValueMethodArgumentResolver {
	
	private final RoundRepository roundRepository;
	
	public UserIdentifierRequestParamResolver(RoundRepository roundRepository) {
		super(null);
		
		this.roundRepository = roundRepository;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return RoundIdentifier.class.isAssignableFrom(parameter.getParameterType());
	}
	
	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		String name = (requestParam != null) ? requestParam.value() : parameter.getParameterName();
		return new NamedValueInfo(name, true, ValueConstants.DEFAULT_NONE);
	}
	
	@Override
	protected Object resolveName(
		String name,
		MethodParameter parameter,
		NativeWebRequest request
	) throws Exception {
////		String identifier = request.getParameter(name);
//		String identifier = request.getAttribute(name, NativeWebRequest.SCOPE_PATH_VARIABLES).toString();
//		
//		// Replace this logic with your custom UserIdentifier creation based on the identifier
//		if ("@current".equals(identifier)) {
//			return new CurrentRoundIdentifier(roundRepository);
//		} else if (identifier.matches("^[0-9]+$")) {
//			// Example: Create IdUserIdentifier
//			return new IdUserIdentifier(Long.parseLong(identifier), userRepository);
//		} else {
//			// Handle invalid input or return a default value
//			return null; // You can throw an exception or handle it as needed
//		}
		
		throw new UnsupportedOperationException();
	}
	
//	@Override
//	protected void handleMissingValue(String name, MethodParameter parameter) throws ServletRequestBindingException {
//		throw new MissingServletRequestParameterException(name, parameter.getParameterType().getSimpleName());
//	}
}