package ext.wis.employee.model;

@SuppressWarnings({"cast", "deprecation", "unchecked"})
public abstract class _Employee extends wt.fc.WTObject implements java.io.Externalizable {
   static final long serialVersionUID = 1;

   static final java.lang.String RESOURCE = "ext.wis.employee.model.modelResource";
   static final java.lang.String CLASSNAME = Employee.class.getName();

   /**
    * 姓名
    *
    * @see ext.wis.employee.model.Employee
    */
   public static final java.lang.String NAME = "name";
   static int NAME_UPPER_LIMIT = -1;
   java.lang.String name;
   /**
    * 姓名
    *
    * @see ext.wis.employee.model.Employee
    */
   public java.lang.String getName() {
      return name;
   }
   /**
    * 姓名
    *
    * @see ext.wis.employee.model.Employee
    */
   public void setName(java.lang.String name) throws wt.util.WTPropertyVetoException {
      nameValidate(name);
      this.name = name;
   }
   void nameValidate(java.lang.String name) throws wt.util.WTPropertyVetoException {
      if (name == null || name.trim().length() == 0)
         throw new wt.util.WTPropertyVetoException("wt.fc.fcResource", wt.fc.fcResource.REQUIRED_ATTRIBUTE,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "name") },
               new java.beans.PropertyChangeEvent(this, "name", this.name, name));
      if (NAME_UPPER_LIMIT < 1) {
         try { NAME_UPPER_LIMIT = (java.lang.Integer) wt.introspection.WTIntrospector.getClassInfo(CLASSNAME).getPropertyDescriptor("name").getValue(wt.introspection.WTIntrospector.UPPER_LIMIT); }
         catch (wt.introspection.WTIntrospectionException e) { NAME_UPPER_LIMIT = 20; }
      }
      if (name != null && !wt.fc.PersistenceHelper.checkStoredLength(name.toString(), NAME_UPPER_LIMIT, true))
         throw new wt.util.WTPropertyVetoException("wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "name"), java.lang.String.valueOf(java.lang.Math.min(NAME_UPPER_LIMIT, wt.fc.PersistenceHelper.DB_MAX_SQL_STRING_SIZE/wt.fc.PersistenceHelper.DB_MAX_BYTES_PER_CHAR)) },
               new java.beans.PropertyChangeEvent(this, "name", this.name, name));
   }

   /**
    * 年龄
    *
    * @see ext.wis.employee.model.Employee
    */
   public static final java.lang.String AGE = "age";
   static int AGE_UPPER_LIMIT = -1;
   java.lang.Integer age;
   /**
    * 年龄
    *
    * @see ext.wis.employee.model.Employee
    */
   public java.lang.Integer getAge() {
      return age;
   }
   /**
    * 年龄
    *
    * @see ext.wis.employee.model.Employee
    */
   public void setAge(java.lang.Integer age) throws wt.util.WTPropertyVetoException {
      ageValidate(age);
      this.age = age;
   }
   void ageValidate(java.lang.Integer age) throws wt.util.WTPropertyVetoException {
      if (age == null)
         throw new wt.util.WTPropertyVetoException("wt.fc.fcResource", wt.fc.fcResource.REQUIRED_ATTRIBUTE,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "age") },
               new java.beans.PropertyChangeEvent(this, "age", this.age, age));
      if (age != null && ((java.lang.Number) age).floatValue() > 100)
         throw new wt.util.WTPropertyVetoException("wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "age"), java.lang.String.valueOf(java.lang.Math.min(AGE_UPPER_LIMIT, wt.fc.PersistenceHelper.DB_MAX_SQL_STRING_SIZE/wt.fc.PersistenceHelper.DB_MAX_BYTES_PER_CHAR)) },
               new java.beans.PropertyChangeEvent(this, "age", this.age, age));
   }

   /**
    * 部门
    *
    * @see ext.wis.employee.model.Employee
    */
   public static final java.lang.String DEPT = "dept";
   static int DEPT_UPPER_LIMIT = -1;
   java.lang.String dept;
   /**
    * 部门
    *
    * @see ext.wis.employee.model.Employee
    */
   public java.lang.String getDept() {
      return dept;
   }
   /**
    * 部门
    *
    * @see ext.wis.employee.model.Employee
    */
   public void setDept(java.lang.String dept) throws wt.util.WTPropertyVetoException {
      deptValidate(dept);
      this.dept = dept;
   }
   void deptValidate(java.lang.String dept) throws wt.util.WTPropertyVetoException {
      if (dept == null || dept.trim().length() == 0)
         throw new wt.util.WTPropertyVetoException("wt.fc.fcResource", wt.fc.fcResource.REQUIRED_ATTRIBUTE,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "dept") },
               new java.beans.PropertyChangeEvent(this, "dept", this.dept, dept));
      if (DEPT_UPPER_LIMIT < 1) {
         try { DEPT_UPPER_LIMIT = (java.lang.Integer) wt.introspection.WTIntrospector.getClassInfo(CLASSNAME).getPropertyDescriptor("dept").getValue(wt.introspection.WTIntrospector.UPPER_LIMIT); }
         catch (wt.introspection.WTIntrospectionException e) { DEPT_UPPER_LIMIT = 10; }
      }
      if (dept != null && !wt.fc.PersistenceHelper.checkStoredLength(dept.toString(), DEPT_UPPER_LIMIT, true))
         throw new wt.util.WTPropertyVetoException("wt.introspection.introspectionResource", wt.introspection.introspectionResource.UPPER_LIMIT,
               new java.lang.Object[] { new wt.introspection.PropertyDisplayName(CLASSNAME, "dept"), java.lang.String.valueOf(java.lang.Math.min(DEPT_UPPER_LIMIT, wt.fc.PersistenceHelper.DB_MAX_SQL_STRING_SIZE/wt.fc.PersistenceHelper.DB_MAX_BYTES_PER_CHAR)) },
               new java.beans.PropertyChangeEvent(this, "dept", this.dept, dept));
   }

   public java.lang.String getConceptualClassname() {
      return CLASSNAME;
   }

   public wt.introspection.ClassInfo getClassInfo() throws wt.introspection.WTIntrospectionException {
      return wt.introspection.WTIntrospector.getClassInfo(getConceptualClassname());
   }

   public java.lang.String getType() {
      try { return getClassInfo().getDisplayName(); }
      catch (wt.introspection.WTIntrospectionException wte) { return wt.util.WTStringUtilities.tail(getConceptualClassname(), '.'); }
   }

   public static final long EXTERNALIZATION_VERSION_UID = 6198761240649336604L;

   public void writeExternal(java.io.ObjectOutput output) throws java.io.IOException {
      output.writeLong( EXTERNALIZATION_VERSION_UID );

      super.writeExternal( output );

      output.writeObject( age );
      output.writeObject( dept );
      output.writeObject( name );
   }

   protected void super_writeExternal_Employee(java.io.ObjectOutput output) throws java.io.IOException {
      super.writeExternal(output);
   }

   public void readExternal(java.io.ObjectInput input) throws java.io.IOException, java.lang.ClassNotFoundException {
      long readSerialVersionUID = input.readLong();
      readVersion( (ext.wis.employee.model.Employee) this, input, readSerialVersionUID, false, false );
   }
   protected void super_readExternal_Employee(java.io.ObjectInput input) throws java.io.IOException, java.lang.ClassNotFoundException {
      super.readExternal(input);
   }

   public void writeExternal(wt.pds.PersistentStoreIfc output) throws java.sql.SQLException, wt.pom.DatastoreException {
      super.writeExternal( output );

      output.setIntObject( "age", age );
      output.setString( "dept", dept );
      output.setString( "name", name );
   }

   public void readExternal(wt.pds.PersistentRetrieveIfc input) throws java.sql.SQLException, wt.pom.DatastoreException {
      super.readExternal( input );

      age = input.getIntObject( "age" );
      dept = input.getString( "dept" );
      name = input.getString( "name" );
   }

   boolean readVersion6198761240649336604L( java.io.ObjectInput input, long readSerialVersionUID, boolean superDone ) throws java.io.IOException, java.lang.ClassNotFoundException {
      if ( !superDone )
         super.readExternal( input );

      age = (java.lang.Integer) input.readObject();
      dept = (java.lang.String) input.readObject();
      name = (java.lang.String) input.readObject();
      return true;
   }

   protected boolean readVersion( Employee thisObject, java.io.ObjectInput input, long readSerialVersionUID, boolean passThrough, boolean superDone ) throws java.io.IOException, java.lang.ClassNotFoundException {
      boolean success = true;

      if ( readSerialVersionUID == EXTERNALIZATION_VERSION_UID )
         return readVersion6198761240649336604L( input, readSerialVersionUID, superDone );
      else
         success = readOldVersion( input, readSerialVersionUID, passThrough, superDone );

      if (input instanceof wt.pds.PDSObjectInput)
         wt.fc.EvolvableHelper.requestRewriteOfEvolvedBlobbedObject();

      return success;
   }
   protected boolean super_readVersion_Employee( _Employee thisObject, java.io.ObjectInput input, long readSerialVersionUID, boolean passThrough, boolean superDone ) throws java.io.IOException, java.lang.ClassNotFoundException {
      return super.readVersion(thisObject, input, readSerialVersionUID, passThrough, superDone);
   }

   boolean readOldVersion( java.io.ObjectInput input, long readSerialVersionUID, boolean passThrough, boolean superDone ) throws java.io.IOException, java.lang.ClassNotFoundException {
      throw new java.io.InvalidClassException(CLASSNAME, "Local class not compatible: stream classdesc externalizationVersionUID="+readSerialVersionUID+" local class externalizationVersionUID="+EXTERNALIZATION_VERSION_UID);
   }
}
