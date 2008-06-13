package com.xpn.xwiki.objects.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.ecs.xhtml.button;
import org.apache.ecs.xhtml.input;
import org.apache.ecs.xhtml.option;
import org.apache.ecs.xhtml.select;
import org.dom4j.Element;

import com.xpn.xwiki.XWikiContext;
import com.xpn.xwiki.XWikiException;
import com.xpn.xwiki.objects.BaseCollection;
import com.xpn.xwiki.objects.BaseProperty;
import com.xpn.xwiki.objects.LargeStringProperty;
import com.xpn.xwiki.objects.meta.PropertyMetaClass;

public class GroupsClass extends ListClass
{

    public GroupsClass(PropertyMetaClass wclass)
    {
        super("groupslist", "Groups List", wclass);
        setSize(6);
        setUsesList(true);
    }

    public GroupsClass()
    {
        this(null);
    }

    @Override
    public List<String> getList(XWikiContext context)
    {
        List<String> list;
        try {
            list = context.getWiki().getGroupService(context).listAllGroups(context);
        } catch (XWikiException e) {
            // TODO add log exception
            list = new ArrayList<String>();

        }

        return list;
    }

    @Override
    public Map getMap(XWikiContext context)
    {
        return new HashMap();
    }

    public boolean isUsesList()
    {
        return (getIntValue("usesList") == 1);
    }

    public void setUsesList(boolean usesList)
    {
        setIntValue("usesList", usesList ? 1 : 0);
    }

    @Override
    public BaseProperty newProperty()
    {
        return new LargeStringProperty();
    }

    @Override
    public BaseProperty fromString(String value)
    {
        BaseProperty prop = newProperty();
        prop.setValue(value);
        return prop;
    }

    @Override
    public BaseProperty fromStringArray(String[] strings)
    {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < strings.length; i++) {
            list.add(strings[i]);
        }
        BaseProperty prop = newProperty();
        prop.setValue(StringUtils.join(list.toArray(), ","));
        return prop;
    }

    public String getText(String value, XWikiContext context)
    {
        if (value.indexOf(":") != -1) {
            return value;
        }
        return value.substring(value.lastIndexOf(".") + 1);
    }

    public static List<String> getListFromString(String value)
    {
        return getListFromString(value, ",", false);        
    }

    @Override
    public void displayEdit(StringBuffer buffer, String name, String prefix, BaseCollection object, XWikiContext context)
    {
        select select = new select(prefix + name, 1);
        select.setMultiple(isMultiSelect());
        select.setSize(getSize());

        List list;
        if (isUsesList()) {
            list = getList(context);
        } else {
            list = new ArrayList();
        }

        List selectlist;

        BaseProperty prop = (BaseProperty) object.safeget(name);
        if (prop == null) {
            selectlist = new ArrayList();
        } else {
            selectlist = getListFromString((String) prop.getValue());
        }

        list.remove("XWiki.XWikiAllGroup");
        list.add(0, "XWiki.XWikiAllGroup");
        if (!context.isMainWiki()) {
            list.remove("xwiki:XWiki.XWikiAllGroup");
            list.add(1, "xwiki:XWiki.XWikiAllGroup");
        }

        // Add options from Set

        for (Iterator it = selectlist.iterator(); it.hasNext();) {
            String value = it.next().toString();
            if (!list.contains(value)) {
                list.add(value);
            }
        }
        for (Iterator it = list.iterator(); it.hasNext();) {
            String value = it.next().toString();
            String display = getText(value, context);
            option option = new option(display, value);
            option.addElement(display);
            if (selectlist.contains(value)) {
                option.setSelected(true);
            }
            select.addElement(option);
        }

        buffer.append(select.toString());

        if (!isUsesList()) {
            input in = new input();
            in.setName(prefix + "newgroup");
            in.setID(prefix + "newgroup");
            in.setSize(15);
            buffer.append("<br />");
            buffer.append(in.toString());

            button button = new button();
            button.setTagText("Add");

            button.setOnClick("addGroup(this.form,'" + prefix + "'); return false;");
            buffer.append(button.toString());
        }

        input in = new input();
        in.setType("hidden");
        in.setName(prefix + name);
        buffer.append(in.toString());
    }

    @Override
    public BaseProperty newPropertyfromXML(Element ppcel)
    {
        String value = ppcel.getText();
        return fromString(value);
    }

}
