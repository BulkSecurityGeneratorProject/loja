import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Cadastros } from './cadastros.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CadastrosService {

    private resourceUrl = SERVER_API_URL + 'api/cadastros';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/cadastros';

    constructor(private http: Http) { }

    create(cadastros: Cadastros): Observable<Cadastros> {
        const copy = this.convert(cadastros);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(cadastros: Cadastros): Observable<Cadastros> {
        const copy = this.convert(cadastros);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Cadastros> {
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
     * Convert a returned JSON object to Cadastros.
     */
    private convertItemFromServer(json: any): Cadastros {
        const entity: Cadastros = Object.assign(new Cadastros(), json);
        return entity;
    }

    /**
     * Convert a Cadastros to a JSON which can be sent to the server.
     */
    private convert(cadastros: Cadastros): Cadastros {
        const copy: Cadastros = Object.assign({}, cadastros);
        return copy;
    }
}
