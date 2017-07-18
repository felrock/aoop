package framework;
import java.beans.*;

/**
   A property editor for enumerated types.
*/
public class EnumEditor extends PropertyEditorSupport
{
   /**
      Constructs a property editor for an enumerated type
      @param cl the class object for the enumerated type
   */
   public EnumEditor(Class cl)
   {
      this.cl = cl;
   }
   /**
    * Check for tags in cl methods, with reflection api.
    * If anything goes wrong(Exception) return null
    * @return String array or null
    * */
   public String[] getTags()
   {
      try
      {
    	 System.out.println("try");
         Object[] values = (Object[]) cl.getMethod("values").invoke(null);
         String[] result = new String[values.length];
         for (int i = 0; i < values.length; i++)
            result[i] = values[i].toString();
         return result;
      }
      catch (Exception ex)
      {
         return null;
      }
   }
   /**
    * @return getValue() as string with toString()
    * */
   public String getAsText(){
      return getValue().toString();
   }
   /**
    * set value to s
    * @param string to replace value in cl
    * */
   public void setAsText(String s){
      setValue(Enum.valueOf(cl, s));
   }

   private Class cl;
}
