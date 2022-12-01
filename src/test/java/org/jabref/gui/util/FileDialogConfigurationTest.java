package org.jabref.gui.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.stage.FileChooser;

import org.jabref.logic.l10n.Localization;
import org.jabref.logic.util.FileType;
import org.jabref.logic.util.StandardFileType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileDialogConfigurationTest {

    @Test
    void testWithValidDirectoryString(@TempDir Path folder) {
        String tempFolder = folder.toAbsolutePath().toString();

        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory(tempFolder).build();

        assertEquals(Optional.of(Path.of(tempFolder)), fileDialogConfiguration.getInitialDirectory());
    }

    @Test
    void testWithValidDirectoryPath(@TempDir Path tempFolder) {
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory(tempFolder).build();

        assertEquals(Optional.of(tempFolder), fileDialogConfiguration.getInitialDirectory());
    }

    @Test
    void testWithNullStringDirectory() {
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory((String) null).build();

        assertEquals(Optional.empty(), fileDialogConfiguration.getInitialDirectory());
    }

    @Test
    void testWithNullPathDirectory() {
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory((Path) null).build();

        assertEquals(Optional.empty(), fileDialogConfiguration.getInitialDirectory());
    }

    @Test
    void testWithNonExistingDirectoryAndParentNull() {
        String tempFolder = "workingDirectory";
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory(tempFolder).build();

        assertEquals(Optional.empty(), fileDialogConfiguration.getInitialDirectory());
    }

    @Test
    void testSingleExtension() {
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withDefaultExtension(StandardFileType.BIBTEX_DB).build();

        FileChooser.ExtensionFilter filter = toFilter(String.format("%1s %2s", "BibTex", Localization.lang("Library")), StandardFileType.BIBTEX_DB);

        assertEquals(filter.getExtensions(), fileDialogConfiguration.getDefaultExtension().getExtensions());
    }

    @Test
    void testWithValidFileNameString() {
        String initialFileName = "testFileName.txt";
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialFileName(initialFileName).build();

        assertEquals(initialFileName, fileDialogConfiguration.getInitialFileName());
    }

    @Test
    void testWithNullDefaultExtension() {
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withDefaultExtension((FileChooser.ExtensionFilter) null).build();

        assertNull(fileDialogConfiguration.getDefaultExtension());
    }

    @Test
    void testWithStringDefaultExtension() {
        String fileTypeDescription = "fileTypeDescriptionTest";
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withDefaultExtension(fileTypeDescription).build();

        FileChooser.ExtensionFilter defaultExtension = new FileChooser.ExtensionFilter("TestDescription", "TestExtensions");
        FileDialogConfiguration fileDialogConfiguration1 = Mockito.spy(fileDialogConfiguration);
        Mockito.doReturn(defaultExtension).when(fileDialogConfiguration1).getDefaultExtension();

        assertEquals(defaultExtension, fileDialogConfiguration1.getDefaultExtension());
    }

    @Test
    void testWithStringDescriptionAndFileTypeDefaultExtension() {
        String description = "description";
        FileType fileType = StandardFileType.BIBTEX_DB;
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withDefaultExtension(description, fileType).build();

        FileChooser.ExtensionFilter defaultExtension = FileFilterConverter.toExtensionFilter(description, fileType);
        FileDialogConfiguration fileDialogConfiguration1 = Mockito.spy(fileDialogConfiguration);
        Mockito.doReturn(defaultExtension).when(fileDialogConfiguration1).getDefaultExtension();

        assertEquals(defaultExtension, fileDialogConfiguration1.getDefaultExtension());
    }

    @Test
    void testWithExtensionFilterList() {
        FileChooser.ExtensionFilter testFilter1 = new FileChooser.ExtensionFilter("TestDescription1", "TestExtensions1");
        FileChooser.ExtensionFilter testFilter2 = new FileChooser.ExtensionFilter("TestDescription2", "TestExtensions2");
        List<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>();
        extensionFilters.add(testFilter1);
        extensionFilters.add(testFilter2);
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .addExtensionFilter(extensionFilters).build();

        assertEquals(extensionFilters, fileDialogConfiguration.getExtensionFilters());
    }

    @Test
    void testWithStringDescriptionAndFileType() {
        String description = "description";
        FileType fileType = StandardFileType.ISI;
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .addExtensionFilter(description, fileType).build();

        List<FileChooser.ExtensionFilter> extensionFilters = new ArrayList<>();
        FileChooser.ExtensionFilter testFilter1 = new FileChooser.ExtensionFilter("TestDescription1", "TestExtensions1");
        FileChooser.ExtensionFilter testFilter2 = new FileChooser.ExtensionFilter("TestDescription2", "TestExtensions2");
        extensionFilters.add(testFilter1);
        extensionFilters.add(testFilter2);
        FileChooser.ExtensionFilter extensionFilter = FileFilterConverter.toExtensionFilter(description, fileType);
        extensionFilters.add(extensionFilter);

        FileDialogConfiguration fileDialogConfiguration1 = Mockito.spy(fileDialogConfiguration);
        Mockito.doReturn(extensionFilters).when(fileDialogConfiguration1).getExtensionFilters();

        assertEquals(extensionFilters, fileDialogConfiguration1.getExtensionFilters());
    }

    @Test
    void testSetSelectedExtensionFilter() {
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TestDescription", "TestExtensions");
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder().build();
        fileDialogConfiguration.setSelectedExtensionFilter(extensionFilter);

        assertEquals(extensionFilter, fileDialogConfiguration.getSelectedExtensionFilter());
    }

    @Test
    void testWithValidDirectoryPathCornerCase() {
        Path tempFolder = Path.of("test/cornerCase/");
        FileDialogConfiguration fileDialogConfiguration = new FileDialogConfiguration.Builder()
                .withInitialDirectory(tempFolder).build();

        assertEquals(Optional.empty(), fileDialogConfiguration.getInitialDirectory());
    }

    private FileChooser.ExtensionFilter toFilter(String description, FileType extension) {
        return new FileChooser.ExtensionFilter(description,
                extension.getExtensions().stream().map(ending -> "*." + ending).collect(Collectors.toList()));
    }
}
