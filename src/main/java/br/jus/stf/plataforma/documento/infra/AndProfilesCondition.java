package br.jus.stf.plataforma.documento.infra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

/**
 * Classe para permitir utilizar o operador AND ao restringir
 * os profiles aos quais uma configuração se aplica. (O Spring
 * não suporta essa funcionalidade diretamente na anotação @Profile ainda).
 * 
 * Classe reaproveitada de https://jira.spring.io/browse/SPR-12458
 * com algumas alterações.
 * 
 * @author Tomas.Godoi
 *
 */
public class AndProfilesCondition implements Condition {

	public static final String VALUE = "value";

	@Override
	public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
		if (context.getEnvironment() == null) {
			return true;
		}
		MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
		if (attrs == null) {
			return true;
		}
		List<String> activeProfiles = Arrays.asList(context.getEnvironment().getActiveProfiles());
		String[] definedProfiles = (String[]) attrs.getFirst(VALUE);
		Set<String> allowedProfiles = new HashSet<>(1);
		Set<String> restrictedProfiles = new HashSet<>(1);
		for (String nextDefinedProfile : definedProfiles) {
			if (!nextDefinedProfile.isEmpty() && nextDefinedProfile.charAt(0) == '!') {
				restrictedProfiles.add(nextDefinedProfile.substring(1, nextDefinedProfile.length()));
				continue;
			}
			allowedProfiles.add(nextDefinedProfile);
		}
		return activeProfiles.containsAll(allowedProfiles) && !CollectionUtils.containsAny(activeProfiles, restrictedProfiles);
	}

}