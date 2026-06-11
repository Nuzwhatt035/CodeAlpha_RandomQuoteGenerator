package com.quotespark.di;

import com.quotespark.data.local.QuoteDao;
import com.quotespark.data.local.QuoteDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class AppModule_ProvideQuoteDaoFactory implements Factory<QuoteDao> {
  private final Provider<QuoteDatabase> databaseProvider;

  public AppModule_ProvideQuoteDaoFactory(Provider<QuoteDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public QuoteDao get() {
    return provideQuoteDao(databaseProvider.get());
  }

  public static AppModule_ProvideQuoteDaoFactory create(Provider<QuoteDatabase> databaseProvider) {
    return new AppModule_ProvideQuoteDaoFactory(databaseProvider);
  }

  public static QuoteDao provideQuoteDao(QuoteDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideQuoteDao(database));
  }
}
