package uk.gov.companieshouse.overseasentitiesapi.utils;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A utility class to compare and apply changes to a Psc object
 *
 * @param <T> The type that contains the data to be changed
 */
public class ChangeManager<T, L, R> {

  private final T objectToAddChanges;
  private final L leftPairData;
  private final R rightPairData;


  /**
   * @param objectToAddChanges The object containing the data that has been changed
   */
  public ChangeManager(T objectToAddChanges, Pair<L, R> pair) {
    if (objectToAddChanges == null) {
      throw new IllegalArgumentException("Invalid input parameter: objectToAddChanges is null.");
    }
    if (pair == null) {
      throw new IllegalArgumentException("Invalid input parameter: pair is null.");
    }

    this.objectToAddChanges = objectToAddChanges;
    this.leftPairData = pair.getLeft();
    this.rightPairData = pair.getRight();
  }

  private static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    }
    if (obj instanceof Object[]) {
      return ((Object[]) obj).length == 0;
    }
    if (obj instanceof List) {
      return ((List<?>) obj).isEmpty();
    }
    if (obj instanceof String) {
      return ((String) obj).isEmpty();
    }
    return false;
  }

  private <P, C, S, U> boolean compareAndBuildChange(
      P proposedData,
      Function<U, C> currentDataGetter,
      U leftOrRightDataFromPair,
      Function<P, S> proposedConverter,
      BiPredicate<P, C> equalityPredicate,
      BiConsumer<T, S> dataSetter) {

    if (isEmpty(proposedData)) {
      return false;
    }
    if (currentDataGetter == null) {
      throw new IllegalArgumentException("Invalid input parameter: currentDataGetter is null.");
    }
    if (equalityPredicate == null) {
      throw new IllegalArgumentException("Invalid input parameter: equalityPredicate is null.");
    }
    if (proposedConverter == null) {
      throw new IllegalArgumentException("Invalid input parameter: proposedConverter is null.");
    }
    if (dataSetter == null) {
      throw new IllegalArgumentException("Invalid input parameter: dataSetter is null.");
    }

    var convertedProposedData = proposedConverter.apply(proposedData);
    C currentData =
        leftOrRightDataFromPair != null ? currentDataGetter.apply(leftOrRightDataFromPair) : null;
    if (leftOrRightDataFromPair == null || !equalityPredicate.test(proposedData, currentData)) {
      dataSetter.accept(objectToAddChanges, convertedProposedData);
      return true;
    }
    return false;
  }


  /**
   * @param proposedData      The data that could be added to objectToAddChanges
   * @param currentDataGetter A getter from the class of the current data
   * @param proposedConverter A function to convert the proposed data to the setter type. If they
   *                          are the same datatype, use Function.identity()
   * @param equalityPredicate A function to compare the proposed and current data. If they are the
   *                          same datatype, use Objects::equals
   * @param dataSetter        A function to set the data on the Psc object
   * @param <P>               The type of the proposed data
   * @param <C>               The type of the current data
   * @param <S>               The type of the setter data
   * @return true if the data has changed, false otherwise
   */
  public <P, C, S> boolean compareAndBuildLeftChange(
      P proposedData,
      Function<L, C> currentDataGetter,
      Function<P, S> proposedConverter,
      BiPredicate<P, C> equalityPredicate,
      BiConsumer<T, S> dataSetter) {
    return compareAndBuildChange(proposedData, currentDataGetter, leftPairData, proposedConverter,
        equalityPredicate, dataSetter);
  }

  /**
   * @param proposedData      The data that could be added to objectToAddChanges
   * @param currentDataGetter A getter from the class of the current data
   * @param proposedConverter A function to convert the proposed data to the setter type. If they
   *                          are the same datatype, use Function.identity()
   * @param equalityPredicate A function to compare the proposed and current data. If they are the
   *                          same datatype, use Objects::equals
   * @param dataSetter        A function to set the data on the Psc object
   * @param <P>               The type of the proposed data
   * @param <C>               The type of the current data
   * @param <S>               The type of the setter data
   * @return true if the data has changed, false otherwise
   */
  public <P, C, S> boolean compareAndBuildRightChange(
      P proposedData,
      Function<R, C> currentDataGetter,
      Function<P, S> proposedConverter,
      BiPredicate<P, C> equalityPredicate,
      BiConsumer<T, S> dataSetter) {
    return compareAndBuildChange(proposedData, currentDataGetter, rightPairData, proposedConverter,
        equalityPredicate, dataSetter);
  }

  /**
   * Compares proposed data to current data and applies the proposed data if they differ. If the
   * proposed data is null or the same as the current data, no changes are made.
   *
   * @param <S>               The type of the proposed and current data
   * @param proposedData      The data that could be added to objectToAddChanges
   * @param currentDataGetter A getter from the class of the current data
   * @param dataSetter        A bi-consumer that accepts the main object and the data to be set on
   *                          it
   * @return true if the proposed data was different from the current data and was therefore set,
   * false otherwise
   */
  public <S> boolean compareAndBuildLeftChange(
      S proposedData,
      Function<L, S> currentDataGetter,
      BiConsumer<T, S> dataSetter) {

    return this.compareAndBuildLeftChange(proposedData, currentDataGetter, Function.identity(),
        Objects::equals,
        dataSetter);
  }

  /**
   * Compares proposed data to current data and applies the proposed data if they differ. If the
   * proposed data is null or the same as the current data, no changes are made.
   *
   * @param <S>               The type of the proposed and current data
   * @param proposedData      The data that could be added to objectToAddChanges
   * @param currentDataGetter A getter from the class of the current data
   * @param dataSetter        A bi-consumer that accepts the main object and the data to be set on
   *                          it
   * @return true if the proposed data was different from the current data and was therefore set,
   * false otherwise
   */
  public <S> boolean compareAndBuildRightChange(
      S proposedData,
      Function<R, S> currentDataGetter,
      BiConsumer<T, S> dataSetter) {

    return this.compareAndBuildRightChange(proposedData, currentDataGetter, Function.identity(),
        Objects::equals, dataSetter);
  }

}
