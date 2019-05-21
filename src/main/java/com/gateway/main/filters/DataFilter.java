package com.gateway.main.filters;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gateway.main.exception.RequestDataFilterException;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DataFilter implements Filter {

	@Override
	public Object processData(String filterParam, Object data) {

		try {
			String requestContent = ((HttpServletRequest) data).getReader().lines()
					.collect(Collectors.joining(""));

			if (requestContent.matches(filterParam)) {

			} else {
				throw new RequestDataFilterException("Data filter not matching pattern");
			}

		} catch (Exception e) {

			throw new RequestDataFilterException(e, e.getMessage());
		}

		System.out.println("executing DataFilter filter");
		return null;
	}

}
