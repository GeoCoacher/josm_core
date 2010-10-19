/*
  Copyright 2008 Stefano Chizzolini. http://clown.stefanochizzolini.it

  Contributors:
    * Stefano Chizzolini (original code developer, http://www.stefanochizzolini.it)

  This file should be part of the source code distribution of "PDF Clown library"
  (the Program): see the accompanying README files for more info.

  This Program is free software; you can redistribute it and/or modify it under the terms
  of the GNU Lesser General Public License as published by the Free Software Foundation;
  either version 3 of the License, or (at your option) any later version.

  This Program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY,
  either expressed or implied; without even the implied warranty of MERCHANTABILITY or
  FITNESS FOR A PARTICULAR PURPOSE. See the License for more details.

  You should have received a copy of the GNU Lesser General Public License along with this
  Program (see README files); if not, go to the GNU website (http://www.gnu.org/licenses/).

  Redistribution and use, with or without modification, are permitted provided that such
  redistributions retain the above copyright notice, license and disclaimer, along with
  this list of conditions.
*/

package it.stefanochizzolini.clown.documents.interaction.actions;

import it.stefanochizzolini.clown.documents.Document;
import it.stefanochizzolini.clown.documents.interaction.navigation.page.Transition;
import it.stefanochizzolini.clown.objects.PdfDirectObject;
import it.stefanochizzolini.clown.objects.PdfIndirectObject;
import it.stefanochizzolini.clown.objects.PdfName;
import it.stefanochizzolini.clown.util.NotImplementedException;

/**
  'Control drawing during a sequence of actions' action [PDF:1.6:8.5.3].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @version 0.0.7
  @since 0.0.7
*/
public class DoTransition
  extends Action
{
  // <class>
  // <dynamic>
  // <constructors>
  /**
    Creates a new transition action within the given document context.
  */
  public DoTransition(
    Document context,
    Transition transition
    )
  {
    super(
      context,
      PdfName.Trans
      );

    setTransition(transition);
  }

  DoTransition(
    PdfDirectObject baseObject,
    PdfIndirectObject container
    )
  {
    super(
      baseObject,
      container
      );
  }
  // </constructors>

  // <interface>
  // <public>
  @Override
  public DoTransition clone(
    Document context
    )
  {throw new NotImplementedException();}

  /**
    Gets the transition effect to be used for the update of the display.
  */
  public Transition getTransition(
    )
  {
    /*
      NOTE: 'Trans' entry MUST exist.
    */
    return new Transition(getBaseDataObject().get(PdfName.Trans),getContainer());
  }

  /**
    @see #getTransition()
  */
  public void setTransition(
    Transition value
    )
  {getBaseDataObject().put(PdfName.Trans, value.getBaseObject());}
  // </public>
  // </interface>
  // </dynamic>
  // </class>
}