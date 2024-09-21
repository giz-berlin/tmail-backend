package com.linagora.tmail.imap.processor;

import com.google.inject.Inject;
import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.processor.NamespaceProcessor;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.metrics.api.MetricFactory;

public class TMailNamespaceProcessor extends NamespaceProcessor {
    @Inject
    public TMailNamespaceProcessor(MailboxManager mailboxManager, StatusResponseFactory factory, MetricFactory metricFactory) {
        super(mailboxManager, factory, metricFactory);
    }
}
