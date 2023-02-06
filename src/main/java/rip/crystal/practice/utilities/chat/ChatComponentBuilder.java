package rip.crystal.practice.utilities.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChatComponentBuilder extends ComponentBuilder {

	private static Field partsField;
	private static Field currField;

	static {
		try {
			currField = ComponentBuilder.class.getDeclaredField("current");
			partsField = ComponentBuilder.class.getDeclaredField("parts");

			currField.setAccessible(true);
			partsField.setAccessible(true);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	public ChatComponentBuilder(String text) {
		super("");
		this.parse(text);
	}

	public TextComponent getCurrent() {
		try {
			return (TextComponent) currField.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setCurrent(TextComponent tc) {
		try {
			currField.set(this, tc);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public List getParts() {
		try {
			return (List) partsField.get(this);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ChatComponentBuilder setCurrentHoverEvent(HoverEvent hoverEvent) {
		this.getCurrent().setHoverEvent(hoverEvent);

		return this;
	}

	public ChatComponentBuilder setCurrentClickEvent(ClickEvent clickEvent) {
		this.getCurrent().setClickEvent(clickEvent);

		return this;
	}

	public ChatComponentBuilder attachToEachPart(HoverEvent hoverEvent) {
		for (Object part : getParts()) {
			TextComponent component = (TextComponent) part;

			if (component.getHoverEvent() == null) {
				component.setHoverEvent(hoverEvent);
			}
		}

		this.getCurrent().setHoverEvent(hoverEvent);

		return this;
	}

	public ChatComponentBuilder attachToEachPart(ClickEvent clickEvent) {
		for (Object part : getParts()) {
			TextComponent component = (TextComponent) part;

			if (component.getClickEvent() == null) {
				component.setClickEvent(clickEvent);
			}
		}

		this.getCurrent().setClickEvent(clickEvent);

		return this;
	}

	public ChatComponentBuilder parse(String text) {
		String regex = "[&§]{1}([a-fA-Fl-oL-O0-9-r]){1}";
		text = text.replaceAll(regex, "§$1");

		if (!Pattern.compile(regex).matcher(text).find()) {
			if (getParts().isEmpty() && getCurrent() != null && getCurrent().getText().isEmpty()) {
				getCurrent().setText(text);
			} else {
				this.append(text);
			}

			return this;
		}

		String[] words = text.split(regex);
		int index = words[0].length();

		for (String word : words) {
			try {
				if (index != words[0].length()) {
					if (getParts().isEmpty() && getCurrent() != null && getCurrent().getText().isEmpty()) {
						getCurrent().setText(word);
					} else {
						this.append(word);
					}

					ChatColor color = ChatColor.getByChar(text.charAt(index - 1));

					if (color == ChatColor.BOLD) {
						this.bold(true);
					} else if (color == ChatColor.STRIKETHROUGH) {
						this.strikethrough(true);
					} else if (color == ChatColor.MAGIC) {
						this.obfuscated(true);
					} else if (color == ChatColor.UNDERLINE) {
						this.underlined(true);
					} else if (color == ChatColor.RESET) {
						this.bold(false);
						this.strikethrough(false);
						this.obfuscated(false);
						this.underlined(false);
					} else {
						this.color(color);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			index += word.length() + 2;
		}

		return this;
	}

	public ChatComponentBuilder append(BaseComponent[] components) {
		for (BaseComponent component : components) {
			append((TextComponent) component);
		}

		return this;
	}

	public ChatComponentBuilder append(TextComponent textComponent) {
		if (textComponent == null) {
			return this;
		}

		String text = textComponent.getText();
		ChatColor color = textComponent.getColor();
		boolean bold = textComponent.isBold();
		boolean underline = textComponent.isUnderlined();
		boolean italic = textComponent.isUnderlined();
		boolean strike = textComponent.isStrikethrough();
		HoverEvent he = textComponent.getHoverEvent();
		ClickEvent ce = textComponent.getClickEvent();

		append(text);
		color(color);
		underlined(underline);
		italic(italic);
		strikethrough(strike);
		event(he);
		event(ce);

		if (textComponent.getExtra() != null) {
			for (BaseComponent bc : textComponent.getExtra()) {
				if (bc instanceof TextComponent) {
					append((TextComponent) bc);
				}
			}
		}

		return this;
	}

	@Override
	public BaseComponent[] create() {
		List<TextComponent> components = new ArrayList<>(getParts());
		components.add(getCurrent());

		TextComponent first = components.get(0);

		if (first.getText().isEmpty()) {
			components.remove(0);
		}

		TextComponent last = components.get(components.size() - 1);

		if (last.getText().isEmpty()) {
			components.remove(components.size() - 1);
		}

		return components.toArray(new BaseComponent[components.size()]);
	}

}
