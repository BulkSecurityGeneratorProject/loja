import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Marcas } from './marcas.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MarcasService {

    private resourceUrl = SERVER_API_URL + 'api/marcas';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/marcas';

    constructor(private http: Http) { }

    create(marcas: Marcas): Observable<Marcas> {
        const copy = this.convert(marcas);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(marcas: Marcas): Observable<Marcas> {
        const copy = this.convert(marcas);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Marcas> {
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
     * Convert a returned JSON object to Marcas.
     */
    private convertItemFromServer(json: any): Marcas {
        const entity: Marcas = Object.assign(new Marcas(), json);
        return entity;
    }

    /**
     * Convert a Marcas to a JSON which can be sent to the server.
     */
    private convert(marcas: Marcas): Marcas {
        const copy: Marcas = Object.assign({}, marcas);
        return copy;
    }
}
