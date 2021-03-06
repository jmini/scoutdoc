/*******************************************************************************
 * Copyright (c) 2012 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/

package scoutdoc.main.mediawiki;

import java.util.Arrays;
import java.util.List;

/**
 * Default implementation for {@link IMediaWikiConfiguration} Correspond to a standard English MediaWiki instance.
 */
public class DefaultMediaWikiConfiguration implements IMediaWikiConfiguration {

  @Override
  public List<String> getRedirectionPrefixes() {
    return Arrays.asList("#REDIRECT");
  }

}
