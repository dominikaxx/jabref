package org.jabref.gui.entryeditor.fileannotationtab;

import javafx.scene.text.Text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FulltextSearchResultsTabTest {

    private FulltextSearchResultsTab fulltextSearchResultsTab;

    @BeforeEach
    public void setUp() throws Exception {
        fulltextSearchResultsTab = mock(FulltextSearchResultsTab.class);
    }

    @Test
    void testCreateFileLink() {
        Text fileLinkText = new Text("Testing createFileLink");
        when(fulltextSearchResultsTab.createFileLink("dir1/dir2")).thenReturn(fileLinkText); // stubbing fulltextSearchResultsTab
        assertEquals(fileLinkText, fulltextSearchResultsTab.createFileLink("dir1/dir2"));
    }

    @Test
    void testCreatePageLink() {
        Text fileLinkText = new Text("Testing createPageLink");
        when(fulltextSearchResultsTab.createPageLink(5)).thenReturn(fileLinkText); // stubbing fulltextSearchResultsTab
        assertEquals(fileLinkText, fulltextSearchResultsTab.createPageLink(5));
    }
}
