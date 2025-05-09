/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.platform.dataaccess.datasource.wizard.service.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import org.pentaho.agilebi.modeler.geo.GeoContext;
import org.pentaho.database.model.DatabaseConnection;
import org.pentaho.gwt.widgets.login.client.AuthenticatedGwtServiceUtil;
import org.pentaho.gwt.widgets.login.client.IAuthenticatedGwtCommand;
import org.pentaho.mantle.client.csrf.CsrfUtil;
import org.pentaho.mantle.client.csrf.IConsumer;
import org.pentaho.metadata.model.Domain;
import org.pentaho.platform.dataaccess.datasource.beans.BusinessData;
import org.pentaho.platform.dataaccess.datasource.beans.LogicalModelSummary;
import org.pentaho.platform.dataaccess.datasource.beans.SerializedResultSet;
import org.pentaho.platform.dataaccess.datasource.wizard.IDatasourceSummary;
import org.pentaho.platform.dataaccess.datasource.wizard.models.DatasourceDTO;
import org.pentaho.platform.dataaccess.datasource.wizard.service.IXulAsyncDSWDatasourceService;
import org.pentaho.platform.dataaccess.datasource.wizard.service.gwt.IGwtDSWDatasourceServiceAsync;
import org.pentaho.ui.xul.XulServiceCallback;

import java.util.List;

public class DSWDatasourceServiceGwtImpl implements IXulAsyncDSWDatasourceService {
  static final String ERROR = "ERROR:";

  static IGwtDSWDatasourceServiceAsync SERVICE;

  static {

    SERVICE = (org.pentaho.platform.dataaccess.datasource.wizard.service.gwt.IGwtDSWDatasourceServiceAsync) GWT
      .create( org.pentaho.platform.dataaccess.datasource.wizard.service.gwt.IGwtDSWDatasourceService.class );
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint( getBaseUrl() );

  }

  /**
   * Returns the context-aware URL to the rpc service
   */
  private static String getBaseUrl() {
    String moduleUrl = GWT.getModuleBaseURL();

    //
    //Set the base url appropriately based on the context in which we are running this client
    //
    if ( moduleUrl.indexOf( "content" ) > -1 ) {
      //we are running the client in the context of a BI Server plugin, so 
      //point the request to the GWT rpc proxy servlet
      String baseUrl = moduleUrl.substring( 0, moduleUrl.indexOf( "content" ) );
      //NOTE: the dispatch URL ("connectionService") must match the bean id for 
      //this service object in your plugin.xml.  "gwtrpc" is the servlet 
      //that handles plugin gwt rpc requests in the BI Server.
      return baseUrl + "gwtrpc/DatasourceService";
    }
    //we are running this client in hosted mode, so point to the servlet 
    //defined in war/WEB-INF/web.xml
    return moduleUrl + "DatasourceService";
  }

  /**
   * Override the service entry point (use only if you know what you are doing)
   */
  public static void setServiceEntryPoint( String serviceEntryPointBase ) {
    ServiceDefTarget endpoint = (ServiceDefTarget) SERVICE;
    endpoint.setServiceEntryPoint( serviceEntryPointBase + "gwtrpc/DatasourceService" );
  }

  public DSWDatasourceServiceGwtImpl() {

  }

  public void doPreview( final String connectionName, final String query, final String previewLimit,
                         final XulServiceCallback<SerializedResultSet> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.doPreview( connectionName, query, previewLimit, callback );
      }
    }, new AsyncCallback<SerializedResultSet>() {

      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( SerializedResultSet arg0 ) {
        xulCallback.success( arg0 );
      }

    } );
  }

  public void generateLogicalModel( final String modelName, final String connectionName, final String dbType,
                                    final String query,
                                    final String previewLimit, final XulServiceCallback<BusinessData> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.generateLogicalModel( modelName, connectionName, dbType, query, previewLimit, callback );
      }
    }, new AsyncCallback<BusinessData>() {

      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( BusinessData arg0 ) {
        xulCallback.success( arg0 );
      }

    } );
  }

  public void saveLogicalModel( final Domain domain, final boolean overwrite,
                                final XulServiceCallback<Boolean> xulCallback ) {

    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( final AsyncCallback callback ) {
        // Lambda expressions are only supported from GWT 2.8 onwards.
        CsrfUtil.callProtected( SERVICE, new IConsumer<IGwtDSWDatasourceServiceAsync>() {
          @Override
          public void accept( IGwtDSWDatasourceServiceAsync service ) {
            service.saveLogicalModel( domain, overwrite, callback );
          }
        } );
      }
    }, new AsyncCallback<Boolean>() {
      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( Boolean arg0 ) {
        xulCallback.success( arg0 );
      }
    } );
  }

  public void hasPermission( final XulServiceCallback<Boolean> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.hasPermission( callback );
      }
    }, new AsyncCallback<Boolean>() {

      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( Boolean arg0 ) {
        xulCallback.success( arg0 );
      }

    } );
  }

  public void deleteLogicalModel( final String domainId, final String modelName,
                                  final XulServiceCallback<Boolean> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( final AsyncCallback callback ) {
        // Lambda expressions are only supported from GWT 2.8 onwards.
        CsrfUtil.callProtected( SERVICE, new IConsumer<IGwtDSWDatasourceServiceAsync>() {
          @Override
          public void accept( IGwtDSWDatasourceServiceAsync service ) {
            service.deleteLogicalModel( domainId, modelName, callback );
          }
        } );
      }
    }, new AsyncCallback<Boolean>() {

      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( Boolean arg0 ) {
        xulCallback.success( arg0 );
      }

    } );
  }

  public void getLogicalModels( final String context,
                                final XulServiceCallback<List<LogicalModelSummary>> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.getLogicalModels( context, callback );
      }
    }, new AsyncCallback<List<LogicalModelSummary>>() {

      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( List<LogicalModelSummary> arg0 ) {
        xulCallback.success( arg0 );
      }

    } );
  }

  public void loadBusinessData( final String domainId, final String modelId,
                                final XulServiceCallback<BusinessData> xulCallback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.loadBusinessData( domainId, modelId, callback );
      }
    }, new AsyncCallback<BusinessData>() {
      public void onFailure( Throwable arg0 ) {
        xulCallback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( BusinessData arg0 ) {
        xulCallback.success( arg0 );
      }
    } );
  }

  public void serializeModelState( final DatasourceDTO dto, final XulServiceCallback<String> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {

        SERVICE.serializeModelState( dto, callback );
      }
    }, new AsyncCallback<String>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( String arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  public void deSerializeModelState( final String dtoStr, final XulServiceCallback<DatasourceDTO> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {
        SERVICE.deSerializeModelState( dtoStr, callback );
      }
    }, new AsyncCallback<DatasourceDTO>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( DatasourceDTO arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  @Override
  public void listDatasourceNames( final XulServiceCallback<List<String>> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {
        SERVICE.listDatasourceNames( callback );
      }
    }, new AsyncCallback<List<String>>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( List<String> arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  @Override
  public void generateQueryDomain( final String name, final String query, final DatabaseConnection connection,
                                   final DatasourceDTO datasourceDTO,
                                   final XulServiceCallback<IDatasourceSummary> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( final AsyncCallback callback ) {
        // Lambda expressions are only supported from GWT 2.8 onwards.
        CsrfUtil.callProtected( SERVICE, new IConsumer<IGwtDSWDatasourceServiceAsync>() {
          @Override
          public void accept( IGwtDSWDatasourceServiceAsync service ) {
            service.generateQueryDomain( name, query, connection, datasourceDTO, callback );
          }
        } );
      }
    }, new AsyncCallback<IDatasourceSummary>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( IDatasourceSummary arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  @Override
  public void getDatasourceIllegalCharacters( final XulServiceCallback<String> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {
        SERVICE.getDatasourceIllegalCharacters( callback );
      }
    }, new AsyncCallback<String>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( String arg0 ) {
        callback.success( arg0 );
      }
    } );
  }

  @Override
  public void getGeoContext( final XulServiceCallback<GeoContext> callback ) {
    AuthenticatedGwtServiceUtil.invokeCommand( new IAuthenticatedGwtCommand() {
      public void execute( AsyncCallback callback ) {
        SERVICE.getGeoContext( callback );
      }
    }, new AsyncCallback<GeoContext>() {
      public void onFailure( Throwable arg0 ) {
        callback.error( arg0.getLocalizedMessage(), arg0 ); //$NON-NLS-1$
      }

      public void onSuccess( GeoContext arg0 ) {
        callback.success( arg0 );
      }
    } );
  }
}
