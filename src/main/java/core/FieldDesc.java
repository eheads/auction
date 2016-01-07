package core;

import io.baratine.db.Cursor;

public interface FieldDesc
{
  boolean isPk();

  String getName();

  String getSqlType();

  Object getValue(Object t);

  void setValue(Object target, Cursor cursor, int index);
}
