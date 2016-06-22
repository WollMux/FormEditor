package de.muenchen.formeditor.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingNode;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.FindToolBar;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.rsta.ui.search.ReplaceToolBar;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.perspective.FXPerspective;
import org.jacpfx.rcp.registry.PerspectiveRegistry;
import org.jacpfx.rcp.util.FXUtil;

import de.muenchen.allg.itd51.wollmux.core.util.L;
import de.muenchen.formeditor.beans.Show;
import de.muenchen.formeditor.exceptions.ApplicationException;
import de.muenchen.formeditor.viewmodels.CodeEditorViewModel;
import de.muenchen.formeditor.viewmodels.CodeTextProperty;

@View(id = "codeeditorcomponent", name = "CodeEditor", active = true, 
	initialTargetLayoutId = FMWorkbench.TARGET_LAYOUT_CENTER)
public class CodeEditorComponent implements FXComponent, SearchListener
{
  private SwingNode root;
  private CollapsibleSectionPanel sectionPanel;
  private TextEditorPane editor;
  private FindDialog findDialog;
  private ReplaceDialog replaceDialog;
  private FindToolBar findToolBar;
  private ReplaceToolBar replaceToolBar;
  private int shortcutKeyMask;

  @Resource
  private Context context;

  @Inject
  private CodeEditorViewModel model;
  
  private CodeTextProperty code;

  @Override
  public Node postHandle(Node node, Message<Event, Object> message)
      throws Exception
  {
    if (message.messageBodyEquals(FXUtil.MessageUtil.INIT))
    {
      code = new CodeTextProperty(editor);
      code.bindBidirectional(model.codeProperty());
    }
    return root;
  }

  @Override
  public Node handle(Message<Event, Object> message) throws Exception
  {
    return null;
  }

  @PostConstruct
  public void onStartComponent(final FXComponentLayout layout,
      final ResourceBundle resourceBundle)
  {
    root = new SwingNode();
    root.setOnMouseReleased(event -> root.requestFocus());
    HBox.setHgrow(root, Priority.ALWAYS);

    try
    {
      SwingUtilities.invokeAndWait(() -> {
        JPanel frame = new JPanel();
        // ((BasicInternalFrameUI) frame.getUI()).setNorthPane(null);
        // frame.setBorder(null);
        shortcutKeyMask = frame.getToolkit().getMenuShortcutKeyMask();
 
        createGui(frame);
 
        root.setContent(frame);
      });
    } catch (HeadlessException | InvocationTargetException
	| InterruptedException e)
    {
      throw new ApplicationException(e);
    }
  }

  protected void onShow(@Observes @Show FXPerspective perspective)
  {
    if (perspective == PerspectiveRegistry.findPerspectiveById(
	context.getParentId(), context.getId()))
    {
      System.out.println(context.getFullyQualifiedId());
    }
  }

  private void createGui(JComponent parent)
  {
    parent.setLayout(new BorderLayout());

    sectionPanel = new CollapsibleSectionPanel();

    editor = new TextEditorPane();
    editor.setBracketMatchingEnabled(true);

    Font font = new Font("Monospaced", Font.PLAIN,
	editor.getFont().getSize() + 2);
    editor.setFont(font);

    SyntaxScheme syntax = editor.getSyntaxScheme();
    syntax.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).foreground = Color.BLUE;
    syntax.getStyle(Token.LITERAL_CHAR).foreground = Color.BLUE;
    syntax.getStyle(Token.SEPARATOR).foreground = Color.DARK_GRAY;
    syntax.getStyle(Token.RESERVED_WORD).font = font;

    RTextScrollPane scrollPane = new RTextScrollPane(editor);

    sectionPanel.add(scrollPane);

    parent.add(sectionPanel, BorderLayout.CENTER);

    CompletionProvider provider = createCompletionProvider();
    AutoCompletion ac = new AutoCompletion(provider);
    ac.install(editor);

    AbstractTokenMakerFactory tokenMakerFactory = (AbstractTokenMakerFactory) TokenMakerFactory
	.getDefaultInstance();
    tokenMakerFactory.putMapping("text/conf",
	"de.muenchen.formeditor.codeeditor.ConfigTokenMaker");
    editor.setSyntaxEditingStyle("text/conf");

    // parent.setJMenuBar(createMenu());

    initDialogs();
    editor.setCaretPosition(0);
  }

  private void initDialogs()
  {
    findDialog = new FindDialog((JFrame) null, this);
    replaceDialog = new ReplaceDialog((JFrame) null, this);

    SearchContext context = findDialog.getSearchContext();
    replaceDialog.setSearchContext(context);

    findToolBar = new FindToolBar(this);
    findToolBar.setSearchContext(context);
    replaceToolBar = new ReplaceToolBar(this);
    replaceToolBar.setSearchContext(context);

    KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcutKeyMask
	| InputEvent.SHIFT_MASK);
    editor.getInputMap().put(keyStroke, "SHOW_FINDTOOLBAR");
    editor.getActionMap().put("SHOW_FINDTOOLBAR",
	sectionPanel.addBottomComponent(keyStroke, findToolBar));
  }

  private JMenuBar createMenu()
  {
    JMenuBar editorMenuBar = new JMenuBar();
    JMenu menu = new JMenu(L.m("Bearbeiten"));
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.UNDO_ACTION)));
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.REDO_ACTION)));
    menu.addSeparator();
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.CUT_ACTION)));
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.COPY_ACTION)));
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.PASTE_ACTION)));
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.DELETE_ACTION)));
    menu.addSeparator();
    menu.add(new JMenuItem(new ShowFindDialogAction()));
    menu.add(new JMenuItem(new ShowReplaceDialogAction()));
    menu.addSeparator();
    menu.add(new JMenuItem(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION)));
    editorMenuBar.add(menu);

    return editorMenuBar;
  }

  private CompletionProvider createCompletionProvider()
  {
    DefaultCompletionProvider provider = new DefaultCompletionProvider();

    provider.addCompletion(new BasicCompletion(provider, "WM"));
    provider.addCompletion(new BasicCompletion(provider, "Eingabefelder"));
    provider.addCompletion(new BasicCompletion(provider, "Fenster"));
    provider.addCompletion(new BasicCompletion(provider, "Formular"));
    provider.addCompletion(new BasicCompletion(provider, "Tab"));
    provider.addCompletion(new BasicCompletion(provider, "TITLE"));
    provider.addCompletion(new BasicCompletion(provider, "CLOSEACTION"));
    provider.addCompletion(new BasicCompletion(provider, "TIP"));
    provider.addCompletion(new BasicCompletion(provider, "LABEL"));
    provider.addCompletion(new BasicCompletion(provider, "TYPE"));
    provider.addCompletion(new BasicCompletion(provider, "READONLY"));
    provider.addCompletion(new BasicCompletion(provider, "AUTOFILL"));
    provider.addCompletion(new BasicCompletion(provider, "DIALOG"));
    provider.addCompletion(new BasicCompletion(provider, "ID"));
    provider.addCompletion(new BasicCompletion(provider, "HOTKEY"));
    provider.addCompletion(new BasicCompletion(provider, "EDIT"));

    return provider;
  }

  @Override
  public void searchEvent(SearchEvent event)
  {
    SearchResult result;

    switch (event.getType())
    {
    case MARK_ALL:
      result = SearchEngine.markAll(editor, event.getSearchContext());
      break;
    case FIND:
      result = SearchEngine.find(editor, event.getSearchContext());
      if (!result.wasFound())
      {
	UIManager.getLookAndFeel().provideErrorFeedback(editor);
      }
      break;
    case REPLACE:
      result = SearchEngine.replace(editor, event.getSearchContext());
      if (!result.wasFound())
      {
	UIManager.getLookAndFeel().provideErrorFeedback(editor);
      }
      break;
    case REPLACE_ALL:
      result = SearchEngine.replaceAll(editor, event.getSearchContext());
      JOptionPane.showMessageDialog(null,
	  String.format(L.m("%d Ersetzung vorgenommen."), result.getCount()));
      break;
    }

  }

  @Override
  public String getSelectedText()
  {
    return editor.getSelectedText();
  }

  private class ShowFindDialogAction extends AbstractAction
  {
    private static final long serialVersionUID = 1L;

    public ShowFindDialogAction()
    {
      super(L.m("Suchen..."));
      putValue(ACCELERATOR_KEY,
	  KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcutKeyMask));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (replaceDialog.isVisible())
      {
	replaceDialog.setVisible(false);
      }
      findDialog.setVisible(true);
    }
  }

  private class ShowReplaceDialogAction extends AbstractAction
  {
    private static final long serialVersionUID = 1L;

    public ShowReplaceDialogAction()
    {
      super(L.m("Ersetzen..."));
      putValue(ACCELERATOR_KEY,
	  KeyStroke.getKeyStroke(KeyEvent.VK_H, shortcutKeyMask));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (findDialog.isVisible())
      {
	findDialog.setVisible(false);
      }
      replaceDialog.setVisible(true);
    }
  }
}
