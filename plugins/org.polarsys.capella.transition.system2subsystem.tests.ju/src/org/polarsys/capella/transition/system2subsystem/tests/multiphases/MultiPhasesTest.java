/*******************************************************************************
 * Copyright (c) 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.transition.system2subsystem.tests.multiphases;

import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.model.helpers.BlockArchitectureExt;
import org.polarsys.capella.core.model.helpers.BlockArchitectureExt.Type;
import org.polarsys.capella.transition.system2subsystem.tests.System2SubsystemTest;
import org.polarsys.capella.transition.system2subsystem.tests.TraceabilityArchitectureSID;
import org.polarsys.capella.transition.system2subsystem.tests.TraceabilitySID;

public abstract class MultiPhasesTest extends System2SubsystemTest {

  public MultiPhasesTest() {
    setKind(Kind.MULTI_PHASES);
  }

  @Override
  protected TraceabilitySID createTraceability() {
    return new TraceabilityArchitectureSID();
  }

  protected EObject retrieveTargetElement(String id, Type architectureType_p) {
    BlockArchitectureExt.Type currentArchitecture = ((TraceabilityArchitectureSID) traceability).getArchitecture();

    try {
      ((TraceabilityArchitectureSID) traceability).setArchitecture(architectureType_p);
      EObject source = traceability.getTracedObject(id);
      assertTrue(source != null);
      return source;
    } finally {
      ((TraceabilityArchitectureSID) traceability).setArchitecture(currentArchitecture);
    }
  }
}
