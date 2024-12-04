package com.linagora.tmail.imap;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import org.apache.james.imap.main.PathConverter;
import org.apache.james.imap.processor.NamespaceSupplier;

public class TMailCrossDomainIMAPModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NamespaceSupplier.class).to(TMailNamespaceSupplier.class).in(Scopes.SINGLETON);
        bind(PathConverter.Factory.class).to(TMailCrossDomainPathConverter.Factory.class).in(Scopes.SINGLETON);
    }
}
