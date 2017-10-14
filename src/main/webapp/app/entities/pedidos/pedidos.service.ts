import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Pedidos } from './pedidos.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PedidosService {

    private resourceUrl = SERVER_API_URL + 'api/pedidos';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/pedidos';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(pedidos: Pedidos): Observable<Pedidos> {
        const copy = this.convert(pedidos);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(pedidos: Pedidos): Observable<Pedidos> {
        const copy = this.convert(pedidos);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Pedidos> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Pedidos.
     */
    private convertItemFromServer(json: any): Pedidos {
        const entity: Pedidos = Object.assign(new Pedidos(), json);
        entity.dataPedido = this.dateUtils
            .convertLocalDateFromServer(json.dataPedido);
        return entity;
    }

    /**
     * Convert a Pedidos to a JSON which can be sent to the server.
     */
    private convert(pedidos: Pedidos): Pedidos {
        const copy: Pedidos = Object.assign({}, pedidos);
        copy.dataPedido = this.dateUtils
            .convertLocalDateToServer(pedidos.dataPedido);
        return copy;
    }
}
