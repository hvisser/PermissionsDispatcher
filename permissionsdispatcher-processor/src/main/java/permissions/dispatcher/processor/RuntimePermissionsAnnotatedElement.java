package permissions.dispatcher.processor;

import com.squareup.javapoet.ClassName;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.ShowsRationale;

import static permissions.dispatcher.processor.ConstantsProvider.CLASS_SUFFIX;
import static permissions.dispatcher.processor.Utils.findMethods;
import static permissions.dispatcher.processor.Utils.findShowsRationalFromValue;
import static permissions.dispatcher.processor.Validator.checkClassName;
import static permissions.dispatcher.processor.Validator.checkDuplicatedPermission;
import static permissions.dispatcher.processor.Validator.checkDuplicatedRationale;
import static permissions.dispatcher.processor.Validator.checkNeedsPermissionSize;
import static permissions.dispatcher.processor.Validator.checkPrivateMethod;

public class RuntimePermissionsAnnotatedElement {

    private final String packageName;

    private final String className;

    private final List<ExecutableElement> needsPermissionMethods;

    private final List<ExecutableElement> showsRationaleMethods;

    public RuntimePermissionsAnnotatedElement(TypeElement element) {
        String qualifiedName = element.getQualifiedName().toString();
        packageName = Utils.getPackageName(qualifiedName);
        className = Utils.getClassName(qualifiedName);
        checkClassName(className);
        needsPermissionMethods = findMethods(element, NeedsPermission.class);
        checkNeedsPermissionSize(needsPermissionMethods);
        checkDuplicatedPermission(needsPermissionMethods);
        checkPrivateMethod(needsPermissionMethods);
        showsRationaleMethods = findMethods(element, ShowsRationale.class);
        checkDuplicatedRationale(showsRationaleMethods);
        checkPrivateMethod(showsRationaleMethods);
    }

    public String getPackageName() {
        return packageName;
    }

    public ClassName getClassName() {
        return ClassName.get(packageName, className);
    }

    public String getDispatchClassName() {
        return className + CLASS_SUFFIX;
    }

    public List<ExecutableElement> getNeedsPermissionMethods() {
        return needsPermissionMethods;
    }

    public ExecutableElement getShowsRationaleMethodFromValue(String value) {
        return findShowsRationalFromValue(value, showsRationaleMethods);
    }

}
