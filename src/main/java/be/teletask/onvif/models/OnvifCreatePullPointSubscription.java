package be.teletask.onvif.models;

import java.time.LocalDateTime;

/**
 * @author BOj
 * @desc 创建拉取点点阅返回类
 * @since 11/8/2024  1:59 PM
 */
public class OnvifCreatePullPointSubscription {

    /**
     * SubscriptionReference (返回的拉取点地址)
     *Endpoint reference of the subscription to be used for pulling the messages.
     **/
    private String subscriptionReference;

    /**
     * CurrentTime (当前时间)
     * Current time of the server for synchronization purposes.
     **/
    private LocalDateTime currentTime;

    /**
     * TerminationTime(拉取点的终结时间)
     * Date time when the PullPoint will be shut down without further pull requests.
     */
    private LocalDateTime  TerminationTime;

    public String getSubscriptionReference() {
        return subscriptionReference;
    }

    public void setSubscriptionReference(String subscriptionReference) {
        this.subscriptionReference = subscriptionReference;
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    public LocalDateTime getTerminationTime() {
        return TerminationTime;
    }

    public void setTerminationTime(LocalDateTime terminationTime) {
        TerminationTime = terminationTime;
    }
}
