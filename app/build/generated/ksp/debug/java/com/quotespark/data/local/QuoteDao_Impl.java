package com.quotespark.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.quotespark.data.Quote;
import com.quotespark.data.QuoteCategory;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class QuoteDao_Impl implements QuoteDao {
  private final RoomDatabase __db;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavoriteStatus;

  private final EntityUpsertionAdapter<Quote> __upsertionAdapterOfQuote;

  private final Converters __converters = new Converters();

  public QuoteDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__preparedStmtOfUpdateFavoriteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE quotes SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__upsertionAdapterOfQuote = new EntityUpsertionAdapter<Quote>(new EntityInsertionAdapter<Quote>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `quotes` (`id`,`text`,`author`,`category`,`isFavorite`,`isFromApi`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Quote entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getText());
        statement.bindString(3, entity.getAuthor());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.isFromApi() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
      }
    }, new EntityDeletionOrUpdateAdapter<Quote>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `quotes` SET `id` = ?,`text` = ?,`author` = ?,`category` = ?,`isFavorite` = ?,`isFromApi` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Quote entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getText());
        statement.bindString(3, entity.getAuthor());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        statement.bindString(4, _tmp);
        final int _tmp_1 = entity.isFavorite() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        final int _tmp_2 = entity.isFromApi() ? 1 : 0;
        statement.bindLong(6, _tmp_2);
        statement.bindString(7, entity.getId());
      }
    });
  }

  @Override
  public Object updateFavoriteStatus(final String id, final boolean isFavorite,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavoriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavoriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavoriteStatusDirectly(final String id, final boolean isFavorite,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavoriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavoriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuote(final Quote quote, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuote.upsert(quote);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertQuotes(final List<Quote> quotes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfQuote.upsert(quotes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Quote>> getFavoriteQuotes() {
    final String _sql = "SELECT * FROM quotes WHERE isFavorite = 1 ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"quotes"}, new Callable<List<Quote>>() {
      @Override
      @NonNull
      public List<Quote> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsFromApi = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromApi");
          final List<Quote> _result = new ArrayList<Quote>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Quote _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final QuoteCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toCategory(_tmp);
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final boolean _tmpIsFromApi;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFromApi);
            _tmpIsFromApi = _tmp_2 != 0;
            _item = new Quote(_tmpId,_tmpText,_tmpAuthor,_tmpCategory,_tmpIsFavorite,_tmpIsFromApi);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getQuotesByCategory(final String category,
      final Continuation<? super List<Quote>> $completion) {
    final String _sql = "SELECT * FROM quotes WHERE category = ? OR ? = 'ALL'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, category);
    _argIndex = 2;
    _statement.bindString(_argIndex, category);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Quote>>() {
      @Override
      @NonNull
      public List<Quote> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsFromApi = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromApi");
          final List<Quote> _result = new ArrayList<Quote>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Quote _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final QuoteCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toCategory(_tmp);
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final boolean _tmpIsFromApi;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFromApi);
            _tmpIsFromApi = _tmp_2 != 0;
            _item = new Quote(_tmpId,_tmpText,_tmpAuthor,_tmpCategory,_tmpIsFavorite,_tmpIsFromApi);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getQuoteById(final String id, final Continuation<? super Quote> $completion) {
    final String _sql = "SELECT * FROM quotes WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Quote>() {
      @Override
      @Nullable
      public Quote call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsFromApi = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromApi");
          final Quote _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final QuoteCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toCategory(_tmp);
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final boolean _tmpIsFromApi;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFromApi);
            _tmpIsFromApi = _tmp_2 != 0;
            _result = new Quote(_tmpId,_tmpText,_tmpAuthor,_tmpCategory,_tmpIsFavorite,_tmpIsFromApi);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getApiQuoteCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM quotes WHERE isFromApi = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getLocalQuotes(final Continuation<? super List<Quote>> $completion) {
    final String _sql = "SELECT * FROM quotes WHERE isFromApi = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Quote>>() {
      @Override
      @NonNull
      public List<Quote> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfText = CursorUtil.getColumnIndexOrThrow(_cursor, "text");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfIsFromApi = CursorUtil.getColumnIndexOrThrow(_cursor, "isFromApi");
          final List<Quote> _result = new ArrayList<Quote>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Quote _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpText;
            _tmpText = _cursor.getString(_cursorIndexOfText);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final QuoteCategory _tmpCategory;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfCategory);
            _tmpCategory = __converters.toCategory(_tmp);
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            final boolean _tmpIsFromApi;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsFromApi);
            _tmpIsFromApi = _tmp_2 != 0;
            _item = new Quote(_tmpId,_tmpText,_tmpAuthor,_tmpCategory,_tmpIsFavorite,_tmpIsFromApi);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
