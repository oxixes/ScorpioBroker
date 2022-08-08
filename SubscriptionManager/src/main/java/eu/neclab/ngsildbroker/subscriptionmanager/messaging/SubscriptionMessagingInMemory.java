package eu.neclab.ngsildbroker.subscriptionmanager.messaging;

import javax.inject.Singleton;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import eu.neclab.ngsildbroker.commons.constants.AppConstants;
import eu.neclab.ngsildbroker.commons.datatypes.InternalNotification;
import eu.neclab.ngsildbroker.commons.datatypes.requests.BaseRequest;
import eu.neclab.ngsildbroker.commons.tools.MicroServiceUtils;
import io.quarkus.arc.profile.IfBuildProfile;
import io.smallrye.mutiny.Uni;

@Singleton
@IfBuildProfile("in-memory")
public class SubscriptionMessagingInMemory extends SubscriptionMessagingBase {

	@Incoming(AppConstants.ENTITY_CHANNEL)
	public Uni<Void> handleEntity(Message<BaseRequest> message) {
		return baseHandleEntity(MicroServiceUtils.deepCopyRequestMessage(message), System.currentTimeMillis());
	}

	@Incoming(AppConstants.INTERNAL_NOTIFICATION_CHANNEL)
	public Uni<Void> handleInternalNotification(Message<InternalNotification> message) {
		return baseHandleInternalNotification(message);
	}
}
