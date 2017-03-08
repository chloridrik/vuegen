package com.chloridrik;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateUtil;
import org.jetbrains.annotations.NonNls;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by jsellam on 07/03/2017.
 */
public class VueGen extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        final DataContext dataContext = e.getDataContext();
        assert project != null;

        PsiDirectory psiDirectory = null;
        PsiElement psiElement = LangDataKeys.PSI_ELEMENT.getData(dataContext);
        if(psiElement instanceof PsiDirectory) {
            psiDirectory = (PsiDirectory)psiElement;
        }

        this.addFileTemplate("Vue view","vue");
        this.addFileTemplate("Vue js","js");
        this.addFileTemplate("Vue scss","scss");

        this.makeFile(psiDirectory.getName(),psiDirectory,"Vue view");
        this.makeFile(psiDirectory.getName(),psiDirectory,"Vue js");
        this.makeFile(psiDirectory.getName(),psiDirectory,"Vue scss");
    }

    void makeFile(String fileName,PsiDirectory psiDirectory,String templatName)
    {
        Properties properties = new Properties(FileTemplateManager.getInstance().getDefaultProperties());
        FileTemplateManager manager = FileTemplateManager.getInstance();
        FileTemplate template = manager.getInternalTemplate(templatName);

        try{
            FileTemplateUtil.createFromTemplate(template, fileName, properties, psiDirectory);
        }
        catch (IncorrectOperationException error)
        {
            throw error;

        }
        catch (Exception error)
        {

        }
    }


    private void addFileTemplate(final @NonNls String name, @NonNls String ext) {
        FileTemplate template = FileTemplateManager.getInstance().getTemplate(name);
        if (template == null) {
            try {
                template =
                        FileTemplateManager.getInstance().addTemplate(name, ext);
                template.setText(FileUtil.loadTextAndClose(
                        new InputStreamReader(this.getClass().getResourceAsStream("fileTemplates/"+ name + "." + ext + ".ft")))
                );
            } catch (IOException ex) {

            }
        }
    }



}
