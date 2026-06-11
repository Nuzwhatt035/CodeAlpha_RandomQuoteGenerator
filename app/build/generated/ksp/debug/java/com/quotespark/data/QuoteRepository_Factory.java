package com.quotespark.data;

import com.quotespark.data.local.QuoteDao;
import com.quotespark.data.remote.QuoteApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class QuoteRepository_Factory implements Factory<QuoteRepository> {
  private final Provider<QuoteDao> quoteDaoProvider;

  private final Provider<QuoteApiService> apiServiceProvider;

  public QuoteRepository_Factory(Provider<QuoteDao> quoteDaoProvider,
      Provider<QuoteApiService> apiServiceProvider) {
    this.quoteDaoProvider = quoteDaoProvider;
    this.apiServiceProvider = apiServiceProvider;
  }

  @Override
  public QuoteRepository get() {
    return newInstance(quoteDaoProvider.get(), apiServiceProvider.get());
  }

  public static QuoteRepository_Factory create(Provider<QuoteDao> quoteDaoProvider,
      Provider<QuoteApiService> apiServiceProvider) {
    return new QuoteRepository_Factory(quoteDaoProvider, apiServiceProvider);
  }

  public static QuoteRepository newInstance(QuoteDao quoteDao, QuoteApiService apiService) {
    return new QuoteRepository(quoteDao, apiService);
  }
}
