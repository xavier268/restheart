/*
 * RESTHeart - the Web API for MongoDB
 * Copyright (C) SoftInstigate Srl
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.restheart.handlers.files;

import io.undertow.server.HttpServerExchange;
import org.restheart.db.GridFsDAO;
import org.restheart.db.GridFsRepository;
import org.restheart.handlers.PipedHttpHandler;
import org.restheart.handlers.RequestContext;
import org.restheart.handlers.collection.DeleteCollectionHandler;

/**
 *
 * @author Andrea Di Cesare {@literal <andrea@softinstigate.com>}
 */
public class DeleteBucketHandler extends DeleteCollectionHandler {

    private final GridFsRepository gridFsDAO;

    /**
     * Creates a new instance of DeleteBucketHandler
     *
     */
    public DeleteBucketHandler() {
        super();
        this.gridFsDAO = new GridFsDAO();
    }

    /**
     * Creates a new instance of DeleteBucketHandler
     *
     * @param next
     */
    public DeleteBucketHandler(PipedHttpHandler next) {
        super(next);
        this.gridFsDAO = new GridFsDAO();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange, RequestContext context) throws Exception {
        if (context.isInError()) {
            next(exchange, context);
            return;
        }
        
        try {
            gridFsDAO.deleteChunksCollection(getDatabase(), context.getDBName(), context.getCollectionName());
        } catch (Throwable t) {
            context.addWarning("error removing the bucket file chunks: " + t.getMessage());
        }

        // delete the bucket collection
        super.handleRequest(exchange, context);
    }
}
