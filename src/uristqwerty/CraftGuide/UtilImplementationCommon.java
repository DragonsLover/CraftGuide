package uristqwerty.CraftGuide;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.ItemStack;
import uristqwerty.CraftGuide.api.ItemFilter;
import uristqwerty.CraftGuide.api.Util;
import uristqwerty.CraftGuide.client.ui.GuiRenderer;

public abstract class UtilImplementationCommon extends Util
{
	public float partialTicks;

	@Override
	public ItemFilter getCommonFilter(Object stack)
	{
		if(stack == null)
		{
			return null;
		}
		else if(stack instanceof ItemStack)
		{
			return new SingleItemFilter((ItemStack)stack);
		}
		else if(stack instanceof List)
		{
			return new MultipleItemFilter((List)stack);
		}
		else if(stack instanceof String)
		{
			return new StringItemFilter((String)stack);
		}
		else
		{
			return null;
		}
	}

	@Override
	public List<String> getItemStackText(ItemStack stack)
	{
		if(stack.getItem() == null)
		{
			List<String> text = new ArrayList<String>(1);
			text.add("\247" + Integer.toHexString(15) + "Error: Item #" + Integer.toString(stack.itemID) + " does not exist");
			return text;
		}

		try
		{
			List list = stack.getItemNameandInformation();

			if(stack.getItemDamage() == -1 && (list.size() < 1 || (list.size() == 1 && (list.get(0) == null || (list.get(0) instanceof String && ((String)list.get(0)).isEmpty())))))
			{
				list = GuiRenderer.fixedItemStack(stack).getItemNameandInformation();
			}

			List<String> text = new ArrayList<String>(list.size());
			boolean first = true;

			for(Object o: list)
			{
				if(o instanceof String)
				{
					if(first)
					{
						text.add("\u00a7" + Integer.toHexString(stack.getRarity().rarityColor) + (String)o);

						if(CraftGuide.alwaysShowID)
						{
							text.add("\u00a77" + "ID: " + stack.itemID + "; data: " + stack.getItemDamage());
						}

						first = false;
					}
					else
					{
						text.add("\u00a77" + (String)o);
					}
				}
			}

			return text;
		}
		catch(Exception e)
		{
			CraftGuideLog.log(e);

			List<String> text = new ArrayList<String>(1);
			text.add("\247" + Integer.toHexString(15) + "Item #" + Integer.toString(stack.itemID) + " data " + Integer.toString(stack.getItemDamage()));
			return text;
		}
	}

	@Override
	public void reloadRecipes()
	{
		CraftGuide.side.reloadRecipes();
	}

	@Override
	public float getPartialTicks()
	{
		return partialTicks;
	}
}
