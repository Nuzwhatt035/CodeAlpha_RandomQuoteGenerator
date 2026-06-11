package com.quotespark.di;

import com.quotespark.data.remote.QuoteApiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import retrofit2.Retrofit;

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
public final class AppModule_ProvideQuoteApiServiceFactory implements Factory<QuoteApiService> {
  private final Provider<Retrofit> retrofitProvider;

  public AppModule_ProvideQuoteApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public QuoteApiService get() {
    return provideQuoteApiService(retrofitProvider.get());
  }

  public static AppModule_ProvideQuoteApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new AppModule_ProvideQuoteApiServiceFactory(retrofitProvider);
  }

  public static QuoteApiService provideQuoteApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideQuoteApiService(retrofit));
  }
}
