package com.quotespark.viewmodel;

import com.quotespark.data.QuoteRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class QuoteViewModel_Factory implements Factory<QuoteViewModel> {
  private final Provider<QuoteRepository> repositoryProvider;

  public QuoteViewModel_Factory(Provider<QuoteRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public QuoteViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static QuoteViewModel_Factory create(Provider<QuoteRepository> repositoryProvider) {
    return new QuoteViewModel_Factory(repositoryProvider);
  }

  public static QuoteViewModel newInstance(QuoteRepository repository) {
    return new QuoteViewModel(repository);
  }
}
