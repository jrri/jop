package libs.safeutil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface MemSafe {
	
	public MemoryRisk[] risk(); 

}

enum MemoryRisk {
	NONE,
	LAZY,
	OBJ_REF_TO_NULL,
	UNREFERENCED_OBJ,
	TEMP_OBJECTS,
	MIXED_CONTEXT,
	RESIZE,
	EXCEPTION
}
