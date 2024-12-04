package com.linagora.tmail.imap;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.linagora.tmail.team.TeamMailbox;
import com.linagora.tmail.team.TeamMailboxNameSpace;
import org.apache.commons.lang3.StringUtils;
import org.apache.james.core.Domain;
import org.apache.james.core.Username;
import org.apache.james.imap.api.display.ModifiedUtf7;
import org.apache.james.imap.api.process.ImapSession;
import org.apache.james.imap.main.PathConverter;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.model.MailboxPath;
import org.apache.james.mailbox.model.search.MailboxQuery;
import org.apache.james.mailbox.model.search.PrefixedRegex;
import org.apache.james.mailbox.model.search.Wildcard;

import java.util.List;
import java.util.Optional;

public class TMailCrossDomainPathConverter extends TMailPathConverter implements PathConverter {

    public static class Factory implements PathConverter.Factory {
        public TMailCrossDomainPathConverter forSession(ImapSession session) {
            return new TMailCrossDomainPathConverter(session.getMailboxSession());
        }

        public TMailCrossDomainPathConverter forSession(MailboxSession session) {
            return new TMailCrossDomainPathConverter(session);
        }
    }

    private TMailCrossDomainPathConverter(MailboxSession mailboxSession) {
        super(mailboxSession);
    }

    public Optional<String> mailboxName(boolean relative, MailboxPath path, MailboxSession session) {
        if (path.getNamespace().equalsIgnoreCase(TeamMailboxNameSpace.TEAM_MAILBOX_NAMESPACE())) {
            // FIXME: hacky implementation
            // Convert local path like MailboxPath(#Teammailbox, team-mailbox@<domain>, <teamXXX>/<folder>)
            // to external representation like #TeamMailbox/<teamXXX>@<domain>/<folder>
            List<String> mailboxNameParts = Splitter.on(session.getPathDelimiter()).splitToList(path.getName());
            String rest = Joiner.on(mailboxSession.getPathDelimiter()).join(Iterables.skip(mailboxNameParts, 1));
            if (!rest.isEmpty()) {
                rest = mailboxSession.getPathDelimiter() + rest;
            }
            Optional<String> res = Optional.of(path.getNamespace() + session.getPathDelimiter() + mailboxNameParts.getFirst()
                    + "@" + path.getUser().getDomainPart().map(Domain::asString).orElse("local").replace(String.valueOf(session.getPathDelimiter()), "__")
                    + rest);
            return res;
        } else {
            return defaultpathConverter.mailboxName(relative, path, session);
        }
    }

    protected MailboxPath getTeamMailboxPath(String absolutePath) {
        // FIXME: hacky implementation
        // Convert absolute path like #TeamMailbox/<teamXXX>@<domain>/<folder> to local representation
        // MailboxPath(#Teammailbox, team-mailbox@<domain>, <teamXXX>/<folder>)

        List<String> mailboxPathParts = Splitter.on(mailboxSession.getPathDelimiter()).splitToList(absolutePath);
        String mailboxName = Joiner.on(mailboxSession.getPathDelimiter()).join(Iterables.skip(mailboxPathParts, 1));
        List<String> mailboxNameParts = Splitter.on("@").splitToList(mailboxName);
        if (mailboxNameParts.size() < 2) {
            return new MailboxPath(TeamMailboxNameSpace.TEAM_MAILBOX_NAMESPACE(), null, mailboxNameParts.getFirst());
        }

        List<String> mailboxNameParts2 = Splitter.on(mailboxSession.getPathDelimiter()).splitToList(mailboxNameParts.get(1));
        String rest = Joiner.on(mailboxSession.getPathDelimiter()).join(Iterables.skip(mailboxNameParts2, 1));
        if (!rest.isEmpty()) {
            rest = mailboxSession.getPathDelimiter() + rest;
        }
        MailboxPath res = new MailboxPath(TeamMailboxNameSpace.TEAM_MAILBOX_NAMESPACE(), teamMailboxUsername(mailboxNameParts2.getFirst().replace("__", String.valueOf(mailboxSession.getPathDelimiter()))), mailboxNameParts.getFirst() + rest);
        return res;
    }

    protected Username teamMailboxUsername(String domain) {
        return Username.from(TeamMailbox.TEAM_MAILBOX_LOCAL_PART(), Optional.of(domain));
    }
}
