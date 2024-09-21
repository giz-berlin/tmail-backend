package com.linagora.tmail.imap.processor;

import jakarta.inject.Inject;

import org.apache.james.imap.api.display.ModifiedUtf7;
import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.api.process.MailboxTyper;
import org.apache.james.imap.message.request.ListRequest;
import org.apache.james.imap.processor.ListProcessor;
import org.apache.james.imap.processor.StatusProcessor;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.SubscriptionManager;
import org.apache.james.mailbox.model.MailboxConstants;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.model.search.MailboxQuery;
import org.apache.james.mailbox.model.search.PrefixedRegex;
import org.apache.james.metrics.api.MetricFactory;

public class TMailListProcessor<T extends ListRequest> extends ListProcessor<T> {
    @Inject
    public TMailListProcessor(MailboxManager mailboxManager, StatusResponseFactory factory,
                              MetricFactory metricFactory, SubscriptionManager subscriptionManager,
                              StatusProcessor statusProcessor, MailboxTyper mailboxTyper) {
        super(mailboxManager, factory, metricFactory, subscriptionManager, statusProcessor, mailboxTyper);
    }

    protected MailboxQuery mailboxQuery(MailboxPath basePath, String mailboxName, MailboxSession mailboxSession) {
        boolean isRelative = (mailboxName.charAt(0) != MailboxConstants.NAMESPACE_PREFIX_CHAR);
        MailboxPath path;
        if (isRelative) {
            path = MailboxPath.forUser(mailboxSession.getUser(), mailboxName);
        } else {
            int delimiterAt = mailboxName.indexOf(mailboxSession.getPathDelimiter());
            String namespace = mailboxName.substring(0, delimiterAt);
            String mailbox = mailboxName.substring(delimiterAt + 1);
            path = new MailboxPath(namespace, mailboxSession.getUser(), mailbox);
        }
        return MailboxQuery.builder()
                .namespace(path.getNamespace())
                .expression(new PrefixedRegex("", ModifiedUtf7.decodeModifiedUTF7(path.getName()), mailboxSession.getPathDelimiter()))
                .build();
    }
}
