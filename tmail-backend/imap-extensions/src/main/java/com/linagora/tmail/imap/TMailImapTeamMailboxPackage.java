package com.linagora.tmail.imap;

import org.apache.james.imap.processor.ListProcessor;
import org.apache.james.imap.processor.NamespaceProcessor;
import org.apache.james.modules.protocols.ImapPackage;
import org.apache.james.utils.ClassName;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.linagora.tmail.imap.processor.TMailListProcessor;
import com.linagora.tmail.imap.processor.TMailNamespaceProcessor;

public class TMailImapTeamMailboxPackage extends ImapPackage.Impl {

    static final ImmutableList<ClassName> REPLACES = ImmutableList.of(
                    ListProcessor.class,
                    NamespaceProcessor.class
            ).stream()
            .map(clazz -> new ClassName(clazz.getCanonicalName()))
            .collect(ImmutableList.toImmutableList());

    @Inject
    public TMailImapTeamMailboxPackage() {
        super(
                ImmutableList.of(
                                TMailNamespaceProcessor.class,
                                TMailListProcessor.class
                        ).stream()
                        .map(clazz -> new ClassName(clazz.getCanonicalName()))
                        .collect(ImmutableList.toImmutableList()),
                ImmutableList.of(),
                ImmutableList.of());
    }
}