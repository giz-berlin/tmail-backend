package com.linagora.tmail.imap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.apache.james.modules.protocols.ImapPackage;
import org.apache.james.utils.ClassName;

public class TMailImapPackage extends ImapPackage.Impl {

    static final ImapPackage AGGREGATE = ImapPackage.and(ImmutableList.of(
            new DefaultNoAuth(),
            new TMailImapAuthPackage(),
            new TMailImapTeamMailboxPackage()
    ));

    /**
     * REMOVE contains the set of Processors, Decoders and Encoders that should not be included in the ImapPackage.
     * The main reason to remove them is to replace them with alternative implementations, without needing to specify the full contents of the DefaultNoAuth ImapPackage minus the ones to replace again.
     */
    static final ImmutableSet<ClassName> REMOVE = ImmutableSet.copyOf(TMailImapTeamMailboxPackage.REPLACES);

    @Inject
    public TMailImapPackage() {
        super(AGGREGATE.processors().stream()
                        .filter(clazz -> !REMOVE.contains(clazz))
                        .collect(ImmutableList.toImmutableList()),
                AGGREGATE.decoders().stream()
                        .filter(clazz -> !REMOVE.contains(clazz))
                        .collect(ImmutableList.toImmutableList()),
                AGGREGATE.encoders().stream()
                        .filter(clazz -> !REMOVE.contains(clazz))
                        .collect(ImmutableList.toImmutableList()));
    }

}
