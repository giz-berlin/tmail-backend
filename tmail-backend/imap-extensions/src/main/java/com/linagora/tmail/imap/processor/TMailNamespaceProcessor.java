package com.linagora.tmail.imap.processor;

import java.util.List;

import org.apache.james.imap.api.message.response.StatusResponseFactory;
import org.apache.james.imap.api.process.ImapSession;
import org.apache.james.imap.message.response.NamespaceResponse;
import org.apache.james.imap.processor.NamespaceProcessor;
import org.apache.james.mailbox.MailboxManager;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.metrics.api.MetricFactory;

import com.google.inject.Inject;

public class TMailNamespaceProcessor extends NamespaceProcessor {
    @Inject
    public TMailNamespaceProcessor(MailboxManager mailboxManager, StatusResponseFactory factory, MetricFactory metricFactory) {
        super(mailboxManager, factory, metricFactory);
    }

    protected List<NamespaceResponse.Namespace> buildPersonalNamespaces(MailboxSession mailboxSession, ImapSession session) {
        return List.of(new NamespaceResponse.Namespace("", mailboxSession.getPathDelimiter()));
    }

    protected List<NamespaceResponse.Namespace> buildOtherUsersSpaces(MailboxSession mailboxSession, ImapSession session) {
        return null;
    }

    protected List<NamespaceResponse.Namespace> buildSharedNamespaces(MailboxSession mailboxSession, ImapSession session) {
        return List.of(new NamespaceResponse.Namespace("#TeamMailbox", mailboxSession.getPathDelimiter()));
    }
}
