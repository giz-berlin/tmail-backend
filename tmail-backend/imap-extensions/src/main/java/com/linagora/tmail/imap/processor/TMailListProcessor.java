package com.linagora.tmail.imap.processor;

import com.google.inject.Inject;
import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.api.process.MailboxTyper;
import org.apache.james.imap.message.request.ListRequest;
import org.apache.james.imap.processor.ListProcessor;
import org.apache.james.imap.processor.StatusProcessor;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.SubscriptionManager;
import org.apache.james.metrics.api.MetricFactory;

public class TMailListProcessor<T extends ListRequest> extends ListProcessor<T> {
    @Inject
    public TMailListProcessor(MailboxManager mailboxManager, StatusResponseFactory factory,
                              MetricFactory metricFactory, SubscriptionManager subscriptionManager,
                              StatusProcessor statusProcessor, MailboxTyper mailboxTyper) {
        super(mailboxManager, factory, metricFactory, subscriptionManager, statusProcessor, mailboxTyper);
    }
}
