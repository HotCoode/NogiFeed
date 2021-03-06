package shts.jp.android.nogifeed.utils;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import shts.jp.android.nogifeed.common.Logger;
import shts.jp.android.nogifeed.models.Entries;
import shts.jp.android.nogifeed.models.Entry;

public class AtomRssParser {

	private static final String TAG = AtomRssParser.class.getSimpleName();

	private static final String TAG_ENTRY = "entry";
	private static final String TAG_TITLE = "title";
	private static final String TAG_LINK = "link";
	private static final String TAG_ID = "id";
	private static final String TAG_PUBLISHED = "published";
	private static final String TAG_UPDATED = "updated";
	private static final String TAG_SUMMARY = "summary";
	private static final String TAG_NAME = "name";
	private static final String TAG_CONTENT = "content";

	private static final String CHARSET_NAME = "UTF-8";

	public static Entries parse(InputStream data) {
		shts.jp.android.nogifeed.models.Entries entries = new shts.jp.android.nogifeed.models.Entries();
		shts.jp.android.nogifeed.models.Entry entry = null;
		XmlPullParser parser = Xml.newPullParser();

		try {
			parser.setInput(data, CHARSET_NAME);
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String tag = null;
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
                    shts.jp.android.nogifeed.common.Logger.v(TAG, "parse start");
					break;

				case XmlPullParser.START_TAG:
					tag = parser.getName();
                    shts.jp.android.nogifeed.common.Logger.v(TAG, "TAG: " + tag);

					if (tag.equals(TAG_ENTRY)) {
						entry = new Entry();
                        Logger.v(TAG, "entry");

					} else if (tag.equals(TAG_TITLE)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.title = text;
                            Logger.v(TAG, "title " + text);
						}

					} else if (tag.equals(TAG_LINK)) {
						if (entry != null) {
							int attrCount = parser.getAttributeCount();
							for (int i = 0; i < attrCount; i++) {
								String text = parser.getAttributeValue(i);
                                entry.link = text;
                                Logger.v(TAG, "link " + text);
							}
						}

					} else if (tag.equals(TAG_ID)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.id = text;
                            Logger.v(TAG, "id " + text);
						}

					} else if (tag.equals(TAG_PUBLISHED)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.published = text;
                            Logger.v(TAG, "published " + text);
						}

					} else if (tag.equals(TAG_UPDATED)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.updated = text;
                            Logger.v(TAG, "updated " + text);
						}

					} else if (tag.equals(TAG_SUMMARY)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.summary = text;
                            Logger.v(TAG, "summary " + text);
						}

					} else if (tag.equals(TAG_NAME)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.name = text;
                            Logger.v(TAG, "name " + text);
						}

					} else if (tag.equals(TAG_CONTENT)) {
						if (entry != null) {
							String text = parser.nextText();
							entry.content = StringUtils.ignoreCdataTagWithCrlf(text); // without CDATA tag
                            Logger.v(TAG, "content " + text);
						}

					} else {
                        Logger.v(TAG, "cannot find tag: " + tag);
					}
					break;

				case XmlPullParser.END_TAG:
					tag = parser.getName();
					if (tag.equals(TAG_ENTRY)) {
						entries.add(entry);
						entry = null;
					}
                    Logger.v(TAG, "END_TAG : " + tag);
					break;
				}
				eventType = parser.next();
			}
            Logger.v(TAG, "parse finish");

		} catch (XmlPullParserException e) {
			Log.e(TAG, "parse error ! : " + e);
			return null;
		} catch (IOException e) {
			Log.e(TAG, "parse error ! : " + e);
			return null;
		}
		return entries;
	}

}
