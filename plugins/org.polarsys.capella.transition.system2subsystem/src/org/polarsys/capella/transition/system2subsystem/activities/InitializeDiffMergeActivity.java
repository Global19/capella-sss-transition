/*******************************************************************************
 * Copyright (c) 2006, 2016 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *    Thales - initial API and implementation
 *******************************************************************************/
package org.polarsys.capella.transition.system2subsystem.activities;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.polarsys.capella.core.data.cs.CsPackage;
import org.polarsys.capella.core.data.fa.FaPackage;
import org.polarsys.capella.core.data.interaction.InteractionPackage;
import org.polarsys.capella.core.transition.common.constants.ITransitionConstants;
import org.polarsys.capella.core.transition.common.handlers.IHandler;
import org.polarsys.capella.core.transition.common.handlers.merge.IMergeHandler;
import org.polarsys.capella.core.transition.common.handlers.traceability.CompoundTraceabilityHandler;
import org.polarsys.capella.core.transition.common.merge.scope.ReferenceModelScope;
import org.polarsys.capella.core.transition.common.merge.scope.TargetModelScope;
import org.polarsys.capella.core.transition.system.topdown.handlers.merge.RealizationLinkCategoryFilter;
import org.polarsys.capella.transition.system2subsystem.constants.ITransitionConstants2;
import org.polarsys.capella.transition.system2subsystem.handlers.filter.UpdateOfCategoryFilter;
import org.polarsys.capella.transition.system2subsystem.handlers.traceability.config.MergeSourceConfiguration;
import org.polarsys.capella.transition.system2subsystem.handlers.traceability.config.MergeTargetConfiguration;
import org.polarsys.kitalpha.cadence.core.api.parameter.ActivityParameters;
import org.polarsys.kitalpha.transposer.rules.handler.rules.api.IContext;

/**
 *
 */
public class InitializeDiffMergeActivity extends org.polarsys.capella.core.transition.system.activities.InitializeDiffMergeActivity {

  @Override
  protected IStatus initializeCategoriesHandlers(IContext context, IMergeHandler handler,
      ActivityParameters activityParams) {

    handler.addCategory(new RealizationLinkCategoryFilter(context), context);
    handler.addCategory(new UpdateOfCategoryFilter(FaPackage.Literals.FUNCTIONAL_CHAIN, context), context);
    handler.addCategory(new UpdateOfCategoryFilter(CsPackage.Literals.PHYSICAL_PATH, context), context);
    handler.addCategory(new UpdateOfCategoryFilter(InteractionPackage.Literals.SCENARIO, context), context);

    return super.initializeCategoriesHandlers(context, handler, activityParams);
  }

  @Override
  protected IHandler createDefaultTraceabilitySourceHandler(IContext context) {
    return new CompoundTraceabilityHandler(new MergeSourceConfiguration());
  }

  @Override
  protected IHandler createDefaultTraceabilityTargetHandler(IContext context) {
    return new CompoundTraceabilityHandler(new MergeTargetConfiguration());
  }
  
  @Override
  protected IStatus initializeReferenceScope(IContext context, ActivityParameters activityParams) {
    super.initializeReferenceScope(context, activityParams);
    
    ReferenceModelScope sourceScope = (ReferenceModelScope) context.get(ITransitionConstants2.MERGE_REFERENCE_SCOPE);
    Set libraries = (Set) context.get(ITransitionConstants2.ROOTS_FOR_LIBRARIES_OF_SOURCE_PROJECT);
    if (libraries != null && !libraries.isEmpty()) {
      for (Object library : libraries) {
        sourceScope.add((EObject) library, true);
      }
    }
    sourceScope.build(getReferenceFilter(context));

    return Status.OK_STATUS;
  }
  
  @Override
  protected IStatus initializeTargetScope(IContext context, ActivityParameters activityParams) {
    super.initializeTargetScope(context, activityParams);
    
    TargetModelScope targetScope = (TargetModelScope) context.get(ITransitionConstants.MERGE_TARGET_SCOPE);
    Set libraries = (Set) context.get(ITransitionConstants2.ROOTS_FOR_LIBRARIES_OF_TARGET_PROJECT);
    if (libraries instanceof Set) {
      for (Object library : libraries) {
        targetScope.add((EObject) library, true);
      }
    }
    targetScope.build(getTargetFilter(context));

    return Status.OK_STATUS;
  }

}
